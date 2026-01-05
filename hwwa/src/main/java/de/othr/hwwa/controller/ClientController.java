package de.othr.hwwa.controller;

import de.othr.hwwa.model.Client;
import de.othr.hwwa.service.ClientServiceI;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientServiceI clientService;

    public ClientController(ClientServiceI clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String listClients(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "name") String sort,
        @RequestParam(defaultValue = "asc") String dir,
        Model model) {

        // TODO: Get current user and only return clients with the same Firma as loggedinuser

        Sort sortObj = dir.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        PageRequest pageable = PageRequest.of(page, size, sortObj);

        Page<Client> clientPage = clientService.findAll(pageable);

        model.addAttribute("clientPage", clientPage);
        model.addAttribute("clients", clientPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", clientPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);

        return "clients";
    }

    @PostMapping
    public String createClient(@ModelAttribute Client client) {
        clientService.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client());
        return "client-form";
    }

    @PostMapping("/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return "redirect:/clients";
    }

    @PostMapping("/{id}")
    public String editClient(@PathVariable Long id, @ModelAttribute Client client) {
        client.setId(id);
        clientService.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id);
        if (client == null) {
            return "redirect:/clients";
        }
        model.addAttribute("client", client);
        return "client-form";
    }
}

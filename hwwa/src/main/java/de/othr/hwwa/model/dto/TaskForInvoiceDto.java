package de.othr.hwwa.model.dto;

import de.othr.hwwa.model.Material;

import java.util.List;

public class TaskForInvoiceDto {

    private long id;
    private String title;
    private String clientName;
    private List<Material> materials;

    public TaskForInvoiceDto(long id, String title, String clientName) {
        this.id = id;
        this.title = title;
        this.clientName = clientName;
    }


    public long getId() {return id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getClient() {return clientName;}

    public void setClient(String clientName) {this.clientName = clientName;}

    public List<Material> getMaterials() {return materials;}

    public void setMaterials(List<Material> materials) {this.materials = materials;}
}

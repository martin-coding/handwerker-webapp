
function removeRow(button) {
    const row = button.closest("tr");
    row.querySelectorAll("input").forEach(i => i.value = "");
    row.remove();
    reindexTables();
}

function addWorkRow() {
    const table = document.getElementById("workTable").querySelector("tbody");
    table.insertAdjacentHTML("beforeend", workRowTemplate());
    reindexTables();
}

function addMaterialRow() {
    const table = document.getElementById("materialTable").querySelector("tbody");
    table.insertAdjacentHTML("beforeend", materialRowTemplate());
    reindexTables();
}

function reindexTables() {
    reindexTable("workTable", "workCosts");
    reindexTable("materialTable", "materials");
}

function reindexTable(tableId, fieldName) {
    const rows = document
        .getElementById(tableId)
        .querySelectorAll("tbody tr");

    rows.forEach((row, index) => {
        row.querySelectorAll("input").forEach(input => {
            input.name = input.name.replace(/\[\d+]/, `[${index}]`);
        });
    });
}

function workRowTemplate() {
    return `
        <tr>
            <td><input type="text" name="workCosts[0].description" class="form-control"/></td>
            <td><input type="number" step="0.1" name="workCosts[0].hoursWorked" class="form-control" value="0.0"/></td>
            <td><input type="number" step="0.01" name="workCosts[0].hourlyRate" class="form-control" value="0.0"/></td>
            <td><span>0.0</span></td>
            <td class="no-print">
                <button type="button" class="btn btn-sm btn-danger" onclick="removeRow(this)">✕</button>
            </td>
        </tr>`;
}

function materialRowTemplate() {
    return `
        <tr>
            <td><input type="text" name="materials[0].description" class="form-control"/></td>
            <td><input type="number" name="materials[0].count" class="form-control" value="0.0"/></td>
            <td><input type="number" step="0.01" name="materials[0].pricePerStock" class="form-control" value="0.0"/></td>
            <td><span>0.0</span></td>
            <td class="no-print">
                <button type="button" class="btn btn-sm btn-danger" onclick="removeRow(this)">✕</button>
            </td>
        </tr>`;
}
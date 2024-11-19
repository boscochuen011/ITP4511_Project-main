/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

var equipments = []; // Define globally

 function openUploadModal() {
            document.getElementById('uploadModal').style.display = 'block';
        }

        function closeUploadModal() {
            document.getElementById('uploadModal').style.display = 'none';
        }

function loadInventory() {
    fetch('InventoryServlet?action=listAvailableJson')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                console.log("Data fetched successfully:", data);
                equipments = data; // Update global variable
                updateTable(data);
            })
            .catch(error => {
                console.error('Error fetching inventory:', error);
                document.getElementById('inventory').textContent = 'Failed to load data: ' + error.message;
            });
}


function updateTable(equipments) {
    console.log('Updating table with data:', equipments);
    let output = '<table border="1"><tr><th>ID</th><th>Name</th><th>Description</th><th>Status</th><th>Location</th><th>Staff Only</th><th>Actions</th></tr>';
    equipments.forEach(equipment => {
        output += '<tr>' +
                '<td>' + equipment.equipmentId + '</td>' +
                '<td>' + equipment.name + '</td>' +
                '<td>' + equipment.description + '</td>' +
                '<td>' + equipment.status + '</td>' +
                '<td>' + equipment.location + '</td>' +
                '<td>' + (equipment.staffOnly ? 'Yes' : 'No') + '</td>' +
                '<td><button onclick="editEquipment(' + equipment.equipmentId + ')">Edit</button></td>' +
                '</tr>';
    });
    output += '</table>';
    document.getElementById('inventory').innerHTML = output;
}



document.addEventListener("DOMContentLoaded", loadInventory);
const inventory = document.getElementById('inventory');

function editEquipment(equipmentId) {
    console.log("Edit button clicked for equipment ID:", equipmentId); // Check if this logs when you click the button
    const equipment = equipments.find(eq => eq.equipmentId === equipmentId);
    if (equipment) {
        const modal = document.getElementById('editModal');
        document.getElementById('editId').value = equipment.equipmentId;
        document.getElementById('editName').value = equipment.name;
        document.getElementById('editDescription').value = equipment.description;
        document.getElementById('editStatus').value = equipment.status;
        document.getElementById('editLocation').value = equipment.location;
        document.getElementById('editStaffOnly').checked = equipment.staffOnly;
        modal.style.display = 'block';
    }
}

// Close the modal
function closeModal() {
    document.getElementById('editModal').style.display = 'none';
}
function submitEdit() {
    const equipment = {
        equipmentId: parseInt(document.getElementById('editId').value),
        name: document.getElementById('editName').value,
        description: document.getElementById('editDescription').value,
        status: document.getElementById('editStatus').value,
        location: document.getElementById('editLocation').value,
        staffOnly: document.getElementById('editStaffOnly').checked
    };

    console.log('Submitting updated equipment:', JSON.stringify(equipment));

    fetch('InventoryServlet?action=update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(equipment)
    })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    console.log('Update successful, updating table...');
                    updateTableEntry(equipment); // 更新表格中的数据显示
                    closeModal(); // 关闭模态框
                } else {
                    console.error('Update failed:', data.message);
                    alert('Update failed: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Network or server error:', error);
                alert('Error communicating with the server. Please try again later.');
            });
}

function updateTableEntry(updatedEquipment) {
    // 找到并更新页面上的设备列表数据
    let rows = document.querySelectorAll('#inventory table tr');
    rows.forEach(row => {
        if (row.cells[0].textContent == updatedEquipment.equipmentId.toString()) {
            row.cells[1].textContent = updatedEquipment.name;
            row.cells[2].textContent = updatedEquipment.description;
            row.cells[3].textContent = updatedEquipment.status;
            row.cells[4].textContent = updatedEquipment.location;
            row.cells[5].textContent = updatedEquipment.staffOnly ? 'Yes' : 'No';
        }
    });
}


function handleResponse(response) {
    if (response.status === 'success') {
        console.log('Update successful!');

    } else {
        console.error('Update failed:', response.message);

    }
}

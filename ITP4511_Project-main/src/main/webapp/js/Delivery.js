/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener('DOMContentLoaded', () => {
    fetch('DeliveriesServlet')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            const table = document.getElementById('deliveriesTable').getElementsByTagName('tbody')[0];
            data.forEach(delivery => {
                let row = table.insertRow();
                row.insertCell().textContent = delivery.deliveryId;
                row.insertCell().textContent = delivery.bookingId;
                row.insertCell().textContent = delivery.courierName; // Update to display courier name
                row.insertCell().textContent = delivery.pickupLocation;
                row.insertCell().textContent = delivery.status;
                row.insertCell().textContent = delivery.scheduledTime ? delivery.scheduledTime : 'N/A';
                row.insertCell().textContent = delivery.deliveredTime ? delivery.deliveredTime : 'N/A';
                row.insertCell().textContent = delivery.createdAt;
                row.insertCell().textContent = delivery.updatedAt;
                let editCell = row.insertCell();
                let editButton = document.createElement('button');
                editButton.textContent = 'Edit';
                editButton.classList.add('edit-btn');
                editButton.addEventListener('click', () => {
                    document.getElementById('editDeliveryId').value = delivery.deliveryId;
                    document.getElementById('editStatus').value = delivery.status;
                    document.getElementById('statusModal').style.display = 'block';
                });
                editCell.appendChild(editButton);
            });
        })
        .catch(error => console.error('Error fetching data: ', error));

    // Modal functionality
    const modal = document.getElementById('statusModal');
    const span = document.getElementsByClassName('close')[0];

    span.onclick = () => {
        modal.style.display = 'none';
    };

    window.onclick = (event) => {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    };

    document.getElementById('statusForm').addEventListener('submit', (event) => {
        event.preventDefault();
        const deliveryId = document.getElementById('editDeliveryId').value;
        const status = document.getElementById('editStatus').value;
        const deliveredTime = status === 'delivered' ? new Date().toISOString() : null;

        fetch('UpdateDeliveryStatusServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ deliveryId, status, deliveredTime })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                location.reload();
            } else {
                alert('Failed to update status');
            }
        })
        .catch(error => console.error('Error updating status: ', error));
    });
});

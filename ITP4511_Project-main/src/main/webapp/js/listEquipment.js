/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */

function searchEquipment() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchBar");
    filter = input.value.toUpperCase();
    table = document.getElementsByTagName("table")[0];
    tr = table.getElementsByTagName("tr");

    for (i = 1; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[0];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }       
    }
}
function toggleWishList(userId, equipmentId, button) {
    fetch('WishListServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'action=toggle&user_id=' + userId + '&equipment_id=' + equipmentId  // 添加 action 参数
    })
    .then(response => response.json())
    .then(data => {
        if (data.added) {
            button.innerHTML = '❤️';
        } else {
            button.innerHTML = '♡';
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
window.onload = function () {
    fetch('WishListServlet?action=view', {
        method: 'GET'
    })
            .then(response => response.json())
            .then(data => {
                const wishlist = new Set(data);
                document.querySelectorAll('button[data-equipment-id]').forEach(button => {
                    const equipmentId = parseInt(button.getAttribute('data-equipment-id'));
                    if (wishlist.has(equipmentId)) {
                        button.innerHTML = '❤️';
                    } else {
                        button.innerHTML = '♡';
                    }
                });
            })
            .catch(error => console.error('Error loading wishlist:', error));
};

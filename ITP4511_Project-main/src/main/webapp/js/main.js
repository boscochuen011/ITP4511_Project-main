document.addEventListener("DOMContentLoaded", function() {
    console.log('DOMContentLoaded event fired'); // Debugging line

    function fetchBookings() {
        fetch('/ITP4511_Project/booking?action=listBookings', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(response => {
            console.log('Response:', response);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Data:', data);
            if (data.success) {
                renderBookings(data.bookings);
            } else {
                console.error('Error loading bookings: ', data.error);
            }
        })
        .catch(error => console.error('Error:', error));
    }

    function renderBookings(bookings) {
        const bookingsContainer = document.querySelector('#bookings-container');
        console.log('bookingsContainer:', bookingsContainer); // Debugging line
        if (!bookingsContainer) {
            console.error('Error: #bookings-container element not found');
            return;
        }

        // Clear the container
        bookingsContainer.innerHTML = ''; 

        if (bookings.length === 0) {
            // No bookings found
            bookingsContainer.innerHTML = '<div class="alert alert-warning" role="alert">You do not have any record.</div>';
            return;
        }

        // Create the table
        const table = document.createElement('table');
        table.className = 'table table-striped';

        // Create the table header
        const thead = document.createElement('thead');
        thead.className = 'table-dark';
        thead.innerHTML = `
            <tr>
                <th>equipment Name</th>
                <th>startTime</th>
                <th>endTime</th>
                <th>status</th>
                <th>action</th>
            </tr>
        `;
        table.appendChild(thead);

        // Create the table body
        const tbody = document.createElement('tbody');
        bookings.forEach(booking => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${booking.equipmentName}</td>
                <td>${booking.startTime}</td>
                <td>${booking.endTime}</td>
                <td>${booking.status}</td>
                <td>
                    ${booking.status === 'approved' ? `<button onclick="markAsReturned('${booking.bookingId}')" class="btn btn-success btn-sm">Mark as Returned</button>` : ''}
                    ${booking.status === 'pending' ? `<button onclick="cancelBooking('${booking.bookingId}')" class="btn btn-danger btn-sm">Cancel</button>` : ''}
                </td>
            `;
            tbody.appendChild(row);
        });
        table.appendChild(tbody);

        // Append the table to the container
        bookingsContainer.appendChild(table);
    }

    // Initial fetch of bookings
    fetchBookings();

    // Expose functions to the global scope
    window.markAsReturned = function(bookingId) {
        updateBooking('update', bookingId);
    }

    window.cancelBooking = function(bookingId) {
        updateBooking('cancel', bookingId);
    }

    function updateBooking(action, bookingId) {
        fetch('/ITP4511_Project/booking', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=${action}&bookingId=${bookingId}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(`Booking ${action}ed successfully!`);
                fetchBookings(); // Fetch and update bookings after action
            } else {
                alert('Error: ' + data.error);
            }
        })
        .catch(error => console.error('Error:', error));
    }
});

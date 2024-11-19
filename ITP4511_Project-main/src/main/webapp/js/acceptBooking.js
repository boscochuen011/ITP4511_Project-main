$(document).ready(function () {
    fetchBookings();

    function fetchBookings() {
        $.ajax({
            url: 'AcceptBookingServlet',
            type: 'GET',
            dataType: 'json',
            success: function (data) {
                let tableContent = '<tr><th>Booking ID</th><th>User ID</th><th>Equipment Name</th><th>Start Time</th><th>End Time</th><th>Delivery Location</th><th>Status</th><th>Actions</th></tr>';
                $.each(data, function (index, booking) {
                    tableContent += '<tr>' +
                            '<td>' + booking.bookingId + '</td>' +
                            '<td>' + booking.userId + '</td>' +
                            '<td>' + booking.equipmentName + '</td>' +
                            '<td>' + booking.startTime + '</td>' +
                            '<td>' + booking.endTime + '</td>' +
                            '<td>' + booking.deliveryLocation + '</td>' +
                            '<td>' + booking.status + '</td>' +
                            '<td><button class="editBtn" data-id="' + booking.bookingId + '">Edit</button></td>' +
                            '</tr>';
                });
                $('#bookingTable').html(tableContent);
                bindEditButtons();
            },
            error: function () {
                alert('Failed to fetch bookings');
            }
        });
    }

    function bindEditButtons() {
        $('.editBtn').on('click', function () {
            let bookingId = $(this).data('id');
            $('#bookingId').val(bookingId);
            fetchDeliveryDetails(bookingId);
        });
    }

    function fetchDeliveryDetails(bookingId) {
        $.ajax({
            url: 'DeliveryDetailsServlet',
            type: 'GET',
            data: {bookingId: bookingId},
            dataType: 'json',
            success: function (data) {
                $('#deliveryId').val(data.deliveryId);
                $('#courierId').text(data.courierId);
                $('#pickupLocation').text(data.pickupLocation);
                $('#deliveryStatus').val(data.status);
                $('#editModal').show();
            },
            error: function () {
                alert('Failed to fetch delivery details');
            }
        });
    }

    $('#updateStatusBtn').on('click', function () {
        let bookingId = $('#bookingId').val();
        let deliveryId = $('#deliveryId').val();
        let bookingStatus = $('#bookingStatus').val();
        let deliveryStatus = $('#deliveryStatus').val();
        $.ajax({
            url: 'AcceptBookingServlet',
            type: 'POST',
            data: {
                bookingId: bookingId,
                deliveryId: deliveryId,
                bookingStatus: bookingStatus,
                deliveryStatus: deliveryStatus
            },
            success: function () {
                alert('Statuses updated successfully');
                $('#editModal').hide();
                fetchBookings();
            },
            error: function () {
                alert('Failed to update statuses');
            }
        });
    });

    $('#closeModalBtn').on('click', function () {
        $('#editModal').hide();
    });
});

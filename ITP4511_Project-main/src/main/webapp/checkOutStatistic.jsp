<%-- 
    Document   : checkOutStatistic
    Created on : 2024?5?17?, ??1:33:05
    Author     : boscochuen
--%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <jsp:include page="header.jsp"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Checkout and Location Statistics</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
        <style>
            .chart-container {
                max-width: 650px;
                height: 400px;
                margin: auto;
            }
            canvas {
                width: 100% !important;
                height: 100% !important;
            }
        </style>

        <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <div class="container">
            <h1>Checkout and Location Statistics</h1>

            <div class="form-group">
                <label for="yearSelect">Select Year:</label>
                <select id="yearSelect" class="form-control">
                    <!-- Year options will be added dynamically via JavaScript -->
                </select>
            </div>

            <div class="form-group">
                <label for="monthSelect">Select Month:</label>
                <select id="monthSelect" class="form-control">
                    <!-- Month options will be added dynamically via JavaScript -->
                </select>
            </div>

            <div class="form-group">
                <button id="toggleChartType" class="btn btn-primary">Toggle Chart Type</button>
            </div>

            <h2>Checkout Statistics</h2>
            <div class="chart-container">
                <canvas id="checkoutChart"></canvas>
            </div>
            <h2>Bookings by Location</h2>
            <div class="chart-container">
                <canvas id="locationChart"></canvas>
            </div>
        </div>

        <script>
            $(document).ready(function () {
                var currentYear = new Date().getFullYear();
                var currentMonth = new Date().getMonth() + 1;
                var chartType = 'bar'; // Default chart type

                // Populate year and month select options
                populateSelectOptions();

                var checkoutChart;
                var locationChart;

                // Fetch initial data
                fetchStatistics(currentYear, currentMonth);

                // Update charts when the selected year or month changes
                $('#yearSelect, #monthSelect').change(function () {
                    fetchStatistics($('#yearSelect').val(), $('#monthSelect').val());
                });

                // Toggle chart type on button click
                $('#toggleChartType').click(function () {
                    chartType = chartType === 'bar' ? 'pie' : 'bar'; // Toggle between 'bar' and 'pie'
                    fetchStatistics($('#yearSelect').val(), $('#monthSelect').val());
                });

                function populateSelectOptions() {
                    // Year options
                    $('#yearSelect').append(`<option value="2024" selected >2024</option>`);
                    $('#yearSelect').append(`<option value="2023">2023</option>`);
                    $('#yearSelect').append(`<option value="2022">2022</option>`);

                    // Month options
                    $('#monthSelect').append(`<option value="1">January</option>`);
                    $('#monthSelect').append(`<option value="2">February</option>`);
                    $('#monthSelect').append(`<option value="3">March</option>`);
                    $('#monthSelect').append(`<option value="4">April</option>`);
                    $('#monthSelect').append(`<option value="5" selected >May</option>`);
                    $('#monthSelect').append(`<option value="6">June</option>`);
                    $('#monthSelect').append(`<option value="7">July</option>`);
                    $('#monthSelect').append(`<option value="8">August</option>`);
                    $('#monthSelect').append(`<option value="9">September</option>`);
                    $('#monthSelect').append(`<option value="10">October</option>`);
                    $('#monthSelect').append(`<option value="11">November</option>`);
                    $('#monthSelect').append(`<option value="12">December</option>`);
                }

                function fetchStatistics(year, month) {
                    // Fetch checkout statistics
                    $.ajax({
                        url: 'CheckOutStatistic',
                        method: 'GET',
                        data: {year: year, month: month},
                        dataType: 'json',
                        success: function (data) {
                            var labels = data.map(stat => stat.equipmentName);
                            var checkouts = data.map(stat => stat.checkouts);

                            var ctx = document.getElementById('checkoutChart').getContext('2d');
                            if (checkoutChart) {
                                checkoutChart.destroy();
                            }
                            checkoutChart = new Chart(ctx, {
                                type: chartType,
                                data: {
                                    labels: labels,
                                    datasets: [{
                                            label: '# of Checkouts',
                                            data: checkouts,
                                            backgroundColor: chartType === 'bar' ? 'rgba(75, 192, 192, 0.2)' : generateColors(labels.length),
                                            borderColor: chartType === 'bar' ? 'rgba(75, 192, 192, 1)' : generateColors(labels.length),
                                            borderWidth: 1
                                        }]
                                },
                                options: {
                                    responsive: true,
                                    maintainAspectRatio: false,
                                    scales: chartType === 'bar' ? {
                                        y: {beginAtZero: true}
                                    } : {}
                                }
                            });
                        }
                    });

                    // Fetch location statistics
                    $.ajax({
                        url: 'LocationStatisticServlet',
                        method: 'GET',
                        data: {year: year, month: month},
                        dataType: 'json',
                        success: function (data) {
                            var locations = data.map(stat => stat.location);
                            var counts = data.map(stat => stat.bookingCount);

                            var ctx = document.getElementById('locationChart').getContext('2d');
                            if (locationChart) {
                                locationChart.destroy();
                            }
                            locationChart = new Chart(ctx, {
                                type: chartType,
                                data: {
                                    labels: locations,
                                    datasets: [{
                                            label: 'Number of Bookings',
                                            data: counts,
                                            backgroundColor: chartType === 'bar' ? 'rgba(153, 102, 255, 0.2)' : generateColors(locations.length),
                                            borderColor: chartType === 'bar' ? 'rgba(153, 102, 255, 1)' : generateColors(locations.length),
                                            borderWidth: 1
                                        }]
                                },
                                options: {
                                    responsive: true,
                                    maintainAspectRatio: false,
                                    scales: chartType === 'bar' ? {
                                        y: {beginAtZero: true}
                                    } : {}
                                }
                            });
                        }
                    });
                }

                function generateColors(count) {
                    const colors = [
                        '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40', 
                        '#C9CBCF', '#FFCD56', '#4DC0C0', '#9966CC', '#36A2EB', '#FF6384',
                        '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40', '#C9CBCF', '#FFCD56', 
                        '#4DC0C0', '#9966CC', '#36A2EB', '#FF6384', '#FFCE56', '#4BC0C0', 
                        '#9966FF', '#FF9F40', '#C9CBCF', '#FFCD56', '#4DC0C0', '#9966CC'
                    ];
                    return colors.slice(0, count);
                }
                
            });
        </script>
    </body>
</html>

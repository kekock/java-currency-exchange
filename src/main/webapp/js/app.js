$(document).ready(function() {
    const host = "http://localhost:8080"

    // Fetch the list of currencies and populate the select element
    function requestCurrencies() {
        $.ajax({
            url: `${host}/currencies`,
            type: "GET",
            dataType: "json",
            success: function (data) {
                const tbody = $('.currencies-table tbody');
                tbody.empty();
                $.each(data, function(index, currency) {
                    const row = $('<tr></tr>');
                    row.append($('<td></td>').text(currency.code));
                    row.append($('<td></td>').text(currency.name));
                    row.append($('<td></td>').text(currency.sign));
                    row.append($('<td></td>').html(
                        `<button class="btn btn-danger btn-sm currency-delete" data-code="${currency.code}">Delete</button> ` +
                        `<button class="btn btn-secondary btn-sm currency-edit" data-bs-toggle="modal" data-bs-target="#edit-currency-modal" data-code="${currency.code}">Edit</button>`
                    ));
                    tbody.append(row);
                });

                const newRateBaseCurrency = $("#new-rate-base-currency");
                newRateBaseCurrency.empty();

                // populate the base currency select element with the list of currencies
                $.each(data, function (index, currency) {
                    newRateBaseCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                const newRateTargetCurrency = $("#new-rate-target-currency");
                newRateTargetCurrency.empty();

                // populate the target currency select element with the list of currencies
                $.each(data, function (index, currency) {
                    newRateTargetCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                const convertBaseCurrency = $("#convert-base-currency");
                convertBaseCurrency.empty();

                // populate the base currency select element with the list of currencies
                $.each(data, function (index, currency) {
                    convertBaseCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                const convertTargetCurrency = $("#convert-target-currency");
                convertTargetCurrency.empty();

                // populate the base currency select element with the list of currencies
                $.each(data, function (index, currency) {
                    convertTargetCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');

                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    }

    requestCurrencies();

    // Function to delete currency
    $(document).on('click', '.currency-delete', function() {
        const code = $(this).data('code');
        $.ajax({
            url: `${host}/currencies/${code}`,
            type: "DELETE",
            success: function() {
                requestCurrencies();
            },
            error: function(jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    });

    // Handle currency edit button click
    $(document).on('click', '.currency-edit', function() {
        const code = $(this).data('code');
        // Fetch the currency details and populate the modal
        $.ajax({
            url: `${host}/currencies/${code}`,
            type: "GET",
            success: function(currency) {
                $('#edit-currency-code').val(currency.code);
                $('#edit-currency-name').val(currency.name);
                $('#edit-currency-sign').val(currency.sign);
            }
        });
    });

    // Handle save changes for currency edit modal
    $('#edit-currency-modal .btn-primary').click(function() {
        const code = $('#edit-currency-code').val();
        const name = $('#edit-currency-name').val();
        const sign = $('#edit-currency-sign').val();
        $.ajax({
            url: `${host}/currencies/${code}`,
            type: "PATCH",
            data: {
                code: code,
                name: name,
                sign: sign
            },
            success: function() {
                requestCurrencies();
                $('#edit-currency-modal').modal('hide');
            },
            error: function(jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    });

    // Fetch exchange rates
    function requestExchangeRates() {
        $.ajax({
            url: `${host}/exchangeRates`,
            type: "GET",
            dataType: "json",
            success: function(response) {
                const tbody = $('.exchange-rates-table tbody');
                tbody.empty();
                $.each(response, function(index, rate) {
                    const row = $('<tr></tr>');
                    const currency = rate.baseCurrency.code + rate.targetCurrency.code;
                    const exchangeRate = rate.rate;
                    row.append($('<td></td>').text(currency));
                    row.append($('<td></td>').text(exchangeRate));
                    row.append($('<td></td>').html(
                        `<button class="btn btn-danger btn-sm exchange-rate-delete" data-pair="${currency}">Delete</button> ` +
                        '<button class="btn btn-secondary btn-sm exchange-rate-edit" data-bs-toggle="modal" data-bs-target="#edit-exchange-rate-modal">Edit</button>'
                    ));
                    tbody.append(row);
                });
            },
            error: function() {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');

                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    }

    requestExchangeRates();

    // Delete exchange rate
    $(document).on('click', '.exchange-rate-delete', function() {
        const pair = $(this).data('pair');
        $.ajax({
            url: `${host}/exchangeRate/${pair}`,
            type: "DELETE",
            success: function() {
                requestExchangeRates();
            },
            error: function(jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    });

    // Handle edit exchange rate button click
    $(document).delegate('.exchange-rate-edit', 'click', function() {
        const pair = $(this).closest('tr').find('td:first').text();
        const exchangeRate = $(this).closest('tr').find('td:eq(1)').text();
        $('#edit-exchange-rate-modal .modal-title').text(`Edit ${pair} Exchange Rate`);
        $('#edit-exchange-rate-modal #exchange-rate-input').val(exchangeRate);
    });

    // Save changes for exchange rate
    $('#edit-exchange-rate-modal .btn-primary').click(function() {
        const pair = $('#edit-exchange-rate-modal .modal-title').text().replace('Edit ', '').replace(' Exchange Rate', '');
        const exchangeRate = $('#edit-exchange-rate-modal #exchange-rate-input').val();
        $.ajax({
            url: `${host}/exchangeRate/${pair}`,
            type: "PATCH",
            data: { rate: exchangeRate },
            success: function() {
                requestExchangeRates();
                $('#edit-exchange-rate-modal').modal('hide');
            },
            error: function(jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    });

    // Add currency form submission
    $("#add-currency").submit(function(e) {
        e.preventDefault();
        $.ajax({
            url: `${host}/currencies`,
            type: "POST",
            data: $("#add-currency").serialize(),
            success: function(data) {
                requestCurrencies();
            },
            error: function(jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
        return false;
    });

    // Add exchange rate form submission
    $("#add-exchange-rate").submit(function(e) {
        e.preventDefault();
        $.ajax({
            url: `${host}/exchangeRates`,
            type: "POST",
            data: $("#add-exchange-rate").serialize(),
            success: function(data) {
                requestExchangeRates();
            },
            error: function(jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
        return false;
    });

    // Convert currency form submission
    $("#convert").submit(function(e) {
        e.preventDefault();
        const baseCurrency = $("#convert-base-currency").val();
        const targetCurrency = $("#convert-target-currency").val();
        const amount = $("#convert-amount").val();

        $.ajax({
            url: `${host}/exchange?from=${baseCurrency}&to=${targetCurrency}&amount=${amount}`,
            type: "GET",
            dataType: "json",
            success: function(response) {
                const convertedAmount = response.convertedAmount;
                $("#convert-result").text(`${amount} ${baseCurrency} = ${convertedAmount} ${targetCurrency}`);
            },
            error: function(jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    });
});

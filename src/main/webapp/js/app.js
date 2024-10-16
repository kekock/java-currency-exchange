$(document).ready(function () {
    const host = "http://localhost:8080"

    function requestCurrencies() {
        $.ajax({
            url: `${host}/currencies`,
            type: "GET",
            dataType: "json",
            success: function (data) {
                const tbody = $('.currencies-table tbody');
                tbody.empty();
                $.each(data, function (index, currency) {
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

                $.each(data, function (index, currency) {
                    newRateBaseCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                newRateBaseCurrency.val("USD");

                const targetCurrencyOptions = newRateBaseCurrency.find('option');
                let selectedTargetCurrency = null;

                targetCurrencyOptions.each(function (i) {
                    if ($(this).val() === "USD" && i + 1 < targetCurrencyOptions.length) {
                        selectedTargetCurrency = $(targetCurrencyOptions[i + 1]).val();
                        return false;
                    }
                });

                const newRateTargetCurrency = $("#new-rate-target-currency");
                newRateTargetCurrency.empty();

                $.each(data, function (index, currency) {
                    newRateTargetCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                newRateTargetCurrency.val(selectedTargetCurrency);

                const convertBaseCurrency = $("#convert-base-currency");
                convertBaseCurrency.empty();

                $.each(data, function (index, currency) {
                    convertBaseCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                convertBaseCurrency.val("USD");

                const convertTargetCurrency = $("#convert-target-currency");
                convertTargetCurrency.empty();

                $.each(data, function (index, currency) {
                    convertTargetCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                convertTargetCurrency.val(selectedTargetCurrency);

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

    $(document).on('click', '.currency-delete', function () {
        const code = $(this).data('code');
        $.ajax({
            url: `${host}/currencies/${code}`,
            type: "DELETE",
            success: function () {
                requestCurrencies();
                requestExchangeRates();
            },
            error: function (jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    });

    $(document).on('click', '.currency-edit', function () {
        const code = $(this).data('code');
        $.ajax({
            url: `${host}/currencies/${code}`,
            type: "GET",
            success: function (currency) {
                $('#edit-currency-code').val(currency.code);
                $('#edit-currency-name').val(currency.name);
                $('#edit-currency-sign').val(currency.sign);
            }
        });
    });

    $('#edit-currency-modal .btn-save').click(function () {
        const code = $('#edit-currency-code').val();
        const name = $('#edit-currency-name').val();
        const sign = $('#edit-currency-sign').val();

        const data = {
            name: name,
            sign: sign
        };

        $.ajax({
            url: `${host}/currencies/${code}`,
            type: "PATCH",
            data: $.param(data),
            contentType: "application/x-www-form-urlencoded",
            success: function () {
                requestCurrencies();
                $('#edit-currency-modal').modal('hide');
            },
            error: function (jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    });

    function requestExchangeRates() {
        $.ajax({
            url: `${host}/exchangeRates`,
            type: "GET",
            dataType: "json",
            success: function (response) {
                const tbody = $('.exchange-rates-table tbody');
                tbody.empty();
                $.each(response, function (index, rate) {
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
            error: function () {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');

                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    }

    requestExchangeRates();

    $(document).on('click', '.exchange-rate-delete', function () {
        const pair = $(this).data('pair');
        $.ajax({
            url: `${host}/exchangeRate/${pair}`,
            type: "DELETE",
            success: function () {
                requestExchangeRates();
            },
            error: function (jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    });

    $(document).delegate('.exchange-rate-edit', 'click', function () {
        const pair = $(this).closest('tr').find('td:first').text();
        const exchangeRate = $(this).closest('tr').find('td:eq(1)').text();
        $('#edit-exchange-rate-modal .modal-title').text(`Edit ${pair} Exchange Rate`);
        $('#edit-exchange-rate-modal #exchange-rate-input').val(exchangeRate);
    });

    $('#edit-exchange-rate-modal .btn-primary').click(function () {
        const pair = $('#edit-exchange-rate-modal .modal-title').text().replace('Edit ', '').replace(' Exchange Rate', '');
        const exchangeRate = $('#edit-exchange-rate-modal #exchange-rate-input').val();
        $.ajax({
            url: `${host}/exchangeRate/${pair}`,
            type: "PATCH",
            data: {rate: exchangeRate},
            success: function () {
                requestExchangeRates();
                $('#edit-exchange-rate-modal').modal('hide');
            },
            error: function (jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    });

    $("#add-currency").submit(function (e) {
        e.preventDefault();
        $.ajax({
            url: `${host}/currencies`,
            type: "POST",
            data: $("#add-currency").serialize(),
            success: function (data) {
                requestCurrencies();
            },
            error: function (jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
        return false;
    });

    $("#add-exchange-rate").submit(function (e) {
        e.preventDefault();
        $.ajax({
            url: `${host}/exchangeRates`,
            type: "POST",
            data: $("#add-exchange-rate").serialize(),
            success: function (data) {
                requestExchangeRates();
            },
            error: function (jqXHR) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
        return false;
    });

    $("#convert").submit(function (e) {
        e.preventDefault();

        const baseCurrency = $("#convert-base-currency").val();
        const targetCurrency = $("#convert-target-currency").val();
        const amount = $("#convert-amount").val();

        $.ajax({
            url: `${host}/exchange?from=${baseCurrency}&to=${targetCurrency}&amount=${amount}`,
            type: "GET",
            success: function (data) {
                $("#convert-converted-amount").val(data.convertedAmount);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');

                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });

        return false;
    });
});

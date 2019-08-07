$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});

function updateTable() {
    $.ajax({
       type: "GET",
       url: context.ajaxUrl + "filter",
       data: $('#filter').serialize()
   }).done(function (data) {
        updateTableWithData(data);
    });
}

function cleanFilter() {
    $.get(context.ajaxUrl, function (data) {
        updateTableWithData(data);
    });
}
let filterForm = $('#filter');

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

function filter() {
    $.ajax({
       type: "GET",
       url: context.ajaxUrl + "filter",
        data: filterForm.serialize()
   }).done(function (data) {
        updateTableWithData(data);
    });
}

function resetFilter() {
    filterForm.find(":input").val("");
    updateTable();
}
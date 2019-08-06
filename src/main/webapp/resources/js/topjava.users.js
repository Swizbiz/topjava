// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                        "asc"
                    ]
                ]
            })
        }
    );
});

$(function () {
    $("input[type=checkbox]").on("change", function () {
        let enabled = $(this).is(":checked");
        $.ajax({
            url: context.ajaxUrl + $(this).data().id,
            method: "POST",
            data: {enable: enabled}
        }).done(function () {
            updateTable();
            successNoty(enabled ? "Enable" : "Disable");
        });
    });
});
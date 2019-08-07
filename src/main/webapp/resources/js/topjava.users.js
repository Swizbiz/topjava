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

function updateTable() {
    $.get(context.ajaxUrl, function (data) {
        updateTableWithData(data);
    });
}

$(function () {
    $("input[type=checkbox]").on("click", function () {
        let checkbox = this;
        let enabled = checkbox.checked;
        $.ajax({
            url: context.ajaxUrl + $(this).data().id,
            method: "POST",
            data: {enable: enabled}
        }).done(function (result) {
            if (result) {
                let color = enabled ? "black" : "darkgray";
                $(checkbox.parentNode.parentNode).css("color", color);
                // updateTable();
                successNoty(enabled ? "Enable" : "Disable");
            }
        }).error(checkbox.checked = !enabled);
    });
});
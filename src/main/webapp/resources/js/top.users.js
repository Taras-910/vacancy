var ctx, userAjaxUrl = "admin/users/";

function enable(chkbox, id) {
    var enabled = chkbox.is(":checked");
//  https://stackoverflow.com/a/22213543/548473
    $.ajax({
        url: userAjaxUrl + id,
        type: "POST",
        data: "enabled=" + enabled
    }).done(function () {
        chkbox.closest("tr").attr("data-userEnabled", enabled);
        successNoty(enabled ? i18n['common.enabled'] : i18n['common.disabled']);
    }).fail(function () {
        $(chkbox).prop("checked", !enabled);
    });
}

// $(document).ready(function () {
$(function () {
    // https://stackoverflow.com/a/5064235/548473
    ctx = {
        ajaxUrl: userAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": userAjaxUrl,
                "dataSrc": ""
            },
            "retrieve": true,
            "paging": false,
            "info": true,
            "language": {
                "search": i18n["common.search"]
            },
            "columns": [
                {
                    "data": "id",
                    "visible": false
                },
                {
                    "data": "name"
                },
                {
                    "data": "email",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return "<a href='mailto:" + data + "'>" + data + "</a>";
                        }
                        return data;
                    }
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return "<input type='checkbox' " + (data ? "checked" : "") + " onclick='enable($(this)," + row.id + ");'/>";
                        }
                        return data;
                    }
                },
                {
                    "data": "registered"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if (!data.enabled) {
                    $(row).attr("data-userEnabled", false);
                }
            }
        }),
        updateTable: function () {
            $.get(userAjaxUrl, updateTableByData);
        }
    };
    makeEditable();
});

// http://api.jquery.com/jQuery.ajax/#using-converters
$.ajaxSetup({
    converters: {
        "text json": function (stringData) {
            var json = JSON.parse(stringData);
            $(json).each(function () {
                this.registered = this.registered.substr(0, 10);
            });
            return json;
        }
    }
});

var ctx, vacancyAjaxUrl = "profile/vacancies/";

function refreshDB() {
    $("#modalTitle").html('refresh');
    $('#detailsRefreshForm').find(":input").val("");
    $("#refreshRow").modal();
}

function sendRefresh() {
    $.ajax({
        type: "POST",
        url: vacancyAjaxUrl + "refresh",
        data: $("#detailsRefreshForm").serialize()
    }).done(function () {
        $("#refreshRow").modal("hide");
//        ctx.updateTable();
        successNoty("Update has started");
    });
}




function vote(chkbox, id) {
    var toVote = chkbox.is(":checked");
    $.ajax({
        url: vacancyAjaxUrl + id,
        type: "POST",
        data: "enabled=" + toVote
    }).done(function () {
        chkbox.closest("tr").attr("vacancy-toVote", toVote);
        successNoty(toVote ? "Select" : "Deselect");
    }).fail(function () {
        $(chkbox).prop("checked", !toVote);
    });
}

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: vacancyAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(vacancyAjaxUrl, updateTableByData);
}

// $(document).ready(function () {
$(function () {
    ctx = {
        ajaxUrl : vacancyAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": vacancyAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "id",
                    "visible": false
                },
                {
                    "data": "url",
                    "visible": false
                },
                {
                    "data": "title"
                },
                {
                    "data": "employerName"
                },
                {
                    "data": "address"
                },
                {
                    "data": "salaryMin"
                },
                {
                    "data": "salaryMax"
                },
                {
                    "data": "skills"
                },
                {
                    "data": "releaseDate",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 10);
                        }
                        return date;
                    }
                },
                {
                    "data": "toVote",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return "<input type='checkbox' " + (data ? "checked" : "") + " onclick='vote($(this)," + row.id + ");'/>";
                        }
                        return data;
                    }
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
                    8,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if (!data.toVote) {
                    $(row).attr("vacancy-toVote", false);
                } else {
                    $(row).attr("vacancy-toVote", true);
                }
            }
        }),
       updateTable: updateFilteredTable
    };
    makeEditable(ctx);
});


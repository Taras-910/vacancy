var ctx, vacancyAjaxUrl = "profile/vacancies/";

function refreshDB() {
    $.ajaxSetup({cacheURL: false});
    $('#detailsRefreshForm').find(":input").val("");
    $("#modalTitle").html('refresh');
    $.get("profile/refresh", function (data) {
        $.each(data, function (key, value) {
            $('#detailsRefreshForm').find("input[name='" + key + "']").val(value);
        });
        $('#refreshRow').modal();
    });
}

function sendRefresh() {
    $.ajax({
        type: "POST",
        url: "profile/refresh",
        data: $("#detailsRefreshForm").serialize()
    }).done(function () {
        $("#refreshRow").modal("hide");
        ctx.updateTable();
        successNoty("Update has started");
    });
}

function addVacancy() {
    $("#modalTitle").html('add');
    form.find(":input").val("");
    $("#addRow").modal();
}

function updateRowVacancy(id) {
    $.ajaxSetup({cacheURL: false});
    $("#modalTitle").html('updateRow');
    $('#detailsUpdateForm').find(":input").val("");
//    $('.not_update_visible').fadeOut();
    $.get(ctx.ajaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            $('#detailsUpdateForm').find("input[name='" + key + "']").val(value);
        });
        $('#updateRow').modal();
    });
}

function updateVacancyTo() {
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl,
        data: $('#detailsUpdateForm').serialize()
    }).done(function () {
        $("#updateRow").modal("hide");
        ctx.updateTable();
        successNoty("Saved");
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
            "pagingType": "full_numbers",
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
                    "data": function (data, type, row) {
                        return '<a href="'+ data.url +'">' + data.title + '</a>'
                    },
                    "width": "80px"
                },
                {
                    "data": "employerName",
                    "width": "80px"
                },
                {
                    "data": "address",
                    "width": "80px"
                },
                {
                    "data": "salaryMin",
                    "width": "20px"
                },
                {
                    "data": "salaryMax",
                    "width": "20px"
                },
                {
                    "data": "skills",
                    "width": "320px"
                },
                {
                    "data": "releaseDate",
                    "render": function (date, type, row) {
                        if (type === "display") {
                            return date.substring(0, 10);
                        }
                        return date;
                    },
                    "width": "40px"
                },
                {
                    "data": "siteName",
                    "visible": false
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
                    "data": "workplace",
                    "visible": false
                },
                {
                    "data": "language",
                    "visible": false
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return "<a onclick='updateRowVacancy(" + row.id + ");'><span class='fa fa-pencil'></span></a>";
                        }
                    }
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

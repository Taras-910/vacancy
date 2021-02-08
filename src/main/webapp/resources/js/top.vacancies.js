var ctx, ajaxUrl = "profile/vacancies/";

function vote(chkbox, id) {
    var toVote = chkbox.is(":checked");
    $.ajax({
        url: ajaxUrl + id,
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
//https://stackoverflow.com/questions/56977572/how-to-set-default-value-in-input-datalist-and-still-have-the-drop-down
    let inputLanguage = document.getElementById('language');
    let inputWorkplace = document.getElementById('workplace');
    if (!inputLanguage.value) {
        inputLanguage.value = 'all';
    }
    if (!inputWorkplace.value) {
        inputWorkplace.value = 'all';
    }
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

function updateRowVacancy(id) {
    $.ajaxSetup({cacheURL: false});
    $('#detailsUpdateForm').find(":input").val("");
    $.get(ajaxUrl + id, function (data) {
        $.each(data, function (key, value) {
            $('#detailsUpdateForm').find("input[name='" + key + "']").val(value);
        });
        $('#updateRow').modal();
    });
}

function updateVacancyTo() {
    $.ajax({
        type: "POST",
        url: ajaxUrl,
        data: $('#detailsUpdateForm').serialize()
    }).done(function () {
        $("#updateRow").modal("hide");
        ctx.updateTable();
        successNoty("Saved");
    });
}

function deleteRowVacancy(id) {
    $.ajaxSetup({cacheURL: false});
    $('#detailsDeleteForm').find(":input[name='id']").val(id);
    $('#deleteRow').modal();
}

function deleteVacancyTo() {
    var id = document.getElementById("idDelete").value;
    $.ajax({
        url: ajaxUrl + id,
        type: "DELETE",
    }).done(function () {
        $("#deleteRow").modal("hide");
        ctx.updateTable();
        successNoty("Deleted");
    });
}

// $(document).ready(function () {
$(function () {
    ctx = {
        ajaxUrl : ajaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": ajaxUrl,
                "dataSrc": ""
            },
            "pagingType": "full_numbers",
            "info": true,
            "deferRender": true,
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
                    }
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
                    "data": function (data, type, row) {
                        if(data.skills === "see the card on the link") {
                            return 'see the card on the <a href="' + data.url + '">link</a>'
                        }
                        return data.skills;
                    }
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
                    "render": function (data, type, row) {  // update
                        if (type === "display") {
                            return "<a onclick='updateRowVacancy(" + row.id + ");'><span class='fa fa-pencil'></span></a>";
                        }
                    }
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": function (data, type, row) {  // delete
                        if (type === "display") {
                            return "<a onclick='deleteRowVacancy(" + row.id + ");'><span class='fa fa-remove'></span></a>";
                        }
                    }
                }
            ],
            "order": [
                [
                    8,
                    "desc"
                ],
                [
                    2,
                    "asc"
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

var ctx, ajaxUrl = "profile/vacancies/";

function vote(chkbox, id) {
    var toVote = chkbox.is(":checked");
    $.ajax({
        url: ajaxUrl + id,
        type: "POST",
        data: "enabled=" + toVote
    }).done(function () {
        chkbox.closest("tr").attr("vacancy-toVote", toVote);
        successNoty(toVote ? i18n['common.select'] : i18n['common.deselected']);
    }).fail(function () {
        $(chkbox).prop("checked", !toVote);
    });
}

function updateFilteredTable() {
//https://stackoverflow.com/questions/56977572/how-to-set-default-value-in-input-datalist-and-still-have-the-drop-down
    let inputLanguage = document.getElementById('language');
    let inputLevel = document.getElementById('level');
    let inputWorkplace = document.getElementById('workplace');
    if (!inputLanguage.value) {
        inputLanguage.value = 'all';
    }
    if (!inputLevel.value) {
        inputLevel.value = 'all';
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
        successNoty(i18n['common.saved']);
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
        successNoty(i18n['common.deleted']);
    });
}

// $(document).ready(function () {
$(function () {
    ctx = {
        ajaxUrl : ajaxUrl,
        datatableApi: $("#datatable").DataTable({
            "iDisplayLength": 10,
            "bPaginate": true,
            "ajax": {
                "url": ajaxUrl,
               "dataSrc": "",
            },
            "columnDefs": [{
                "defaultContent": "-",
                "targets": "_all"
            }],
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
                    "data": "url",
                    "visible": false
                },
                {
                    "data": function (data, type, row) {
                        return '<a href="'+ data.url +'">' + data.title + '</a>'
                    }
                },
                {
                    "data": function (data, type, row) {
                        if(data.employerName === "see the card") {
                            return 'see the <a href="' + data.url + '">card</a>'
                        }
                        return data.employerName;
                    }
                },
                {
                    "data": function (data, type, row) {
                        if(data.address === "see the card") {
                            return 'see the <a href="' + data.url + '">card</a>'
                        }
                        return data.address;
                    }
                },
                {
                    "data": function (data, type, row) {
                        if (data.salaryMin === 1 && type === "display") {
                            return '<a href="' + data.url + '">-</a>'
                        }
                        if (data.salaryMin > 1 && type === "display") {
                            return data.salaryMin / 100;
                        }
                        return data.salaryMin;
                    },
                    className: "uniqueClassName"
                },
                {
                    "data": function (data, type, row) {
                        if (data.salaryMax === 1 && type === "display") {
                            return '<a href="' + data.url + '">-</a>'
                        }
                        if (data.salaryMax > 1 && type === "display") {
                            return data.salaryMax / 100;
                        }
                        return data.salaryMax;
                    },
                    className: "uniqueClassName"
                },
                {
                    "data": function (data, type, row) {
                        if(data.skills === "see the card") {
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
                    8, "desc"

                ],
                [
                    3, "asc"
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

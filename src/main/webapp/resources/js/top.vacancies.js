var ctx, vacancyAjaxUrl = "profile/vacancies/";
// var form = $('#detailsForm');
// var ajaxUrl = vacancyAjaxUrl;
// var datatableApi;

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

/*
function save() {
    $.ajax({
        type: "POST",
        url: vacancyAjaxUrl,
        data: form.serialize()
    }).done(function () {
        $("#editRow").modal("hide");
        updateFilteredTable();
        successNoty("Saved");
    });
}
*/

/*
function deleteRow(id) {
    if (confirm('Are you sure?')) {
        $.ajax({
            url: vacancyAjaxUrl + id,
            type: "DELETE"
        }).done(function () {
            updateFilteredTable();
            successNoty("Deleted");
        });
    }
}
*/

/*
function updateTableByData(data) {
    ctx.datatableApi.clear().rows.add(data).draw();
}
*/

// $(document).ready(function () {
$(function () {
    ctx = {
        ajaxUrl : vacancyAjaxUrl,
        datatableApi: $("#datatable").DataTable({
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
                    "data": "releaseDate"
                },
                {
                    "data": "toVote"
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
                    8,
                    "desc"
                ]
            ]
        }),
       updateTable: updateFilteredTable
    };
    makeEditable(ctx);
});

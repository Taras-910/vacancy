var freshenUrl = "profile/freshen";

function refreshDB() {
    $('#detailsRefreshForm').find(":input").val("");
    $("#refreshRow").modal();
}

function sendRefresh() {
    $.ajax({
        type: "POST",
        url: freshenUrl,
        data: $("#detailsRefreshForm").serialize()
    }).done(function () {
        $("#refreshRow").modal("hide");
        ctx.updateTable();
        successNoty("Update has finished ");
    });
}

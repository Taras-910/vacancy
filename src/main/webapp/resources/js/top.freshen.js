var freshenUrl = "profile/freshen";
let spinner = document.getElementById("spinner");
let spinner2 = document.getElementById("spinner2");

function refreshDB() {
    spinner.style.visibility = 'hidden';
    spinner2.style.visibility = 'hidden';
    $('#detailsRefreshForm').find(":input").val("");
    $("#refreshRow").modal();
}

function sendRefresh() {
    spinner.style.visibility = 'visible';
    spinner2.style.visibility = 'visible';
    $.ajax({
        type: "POST",
        url: freshenUrl,
        data: $("#detailsRefreshForm").serialize()
    }).done(function () {
        spinner.style.visibility = 'hidden';
        spinner2.style.visibility = 'hidden';
        $("#refreshRow").modal("hide");
        ctx.updateTable();
        successNoty("Update has finished ");
    });
}

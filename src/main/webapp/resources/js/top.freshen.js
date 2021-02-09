var freshenUrl = "profile/freshen";
let spinner1 = document.getElementById("spinner1");
let spinner2 = document.getElementById("spinner2");

function refreshDB() {
    spinner1.style.visibility = 'hidden';
    spinner2.style.visibility = 'hidden';
    $('#detailsRefreshForm').find(":input").val("");
    $("#refreshRow").modal();
}

function sendRefresh() {
    spinner1.style.visibility = 'visible';
    spinner2.style.visibility = 'visible';
    $.ajax({
        type: "POST",
        url: freshenUrl,
        data: $("#detailsRefreshForm").serialize()
    }).done(function () {
        let languageFilter = document.getElementById('language');
        let workplaceFilter = document.getElementById('workplace');
        languageFilter.value = document.getElementById('languageTask').value;
        workplaceFilter.value = document.getElementById('workplaceTask').value;
        $("#refreshRow").modal("hide");
        ctx.updateTable();
        successNoty("Update has finished ");
    });
}

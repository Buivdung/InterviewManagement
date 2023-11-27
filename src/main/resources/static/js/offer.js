$(document).ready(function (){
    function getCandidate() {
        console.log(candidateId.val())
        window.location.href
        $.get("/offer/getResultCandidate/"+ candidateId.val()).done(function (data) {
            $("#status").text(data.estatus)
            $("#interviewers").text(data.interviews.toString())
            $("#interviewer-notes").text(data.notes)
        })
    }
    let candidateId = $("#candidateId").on("change",getCandidate);
})
function approveStatus(id){
    let notes = $("#notes").val();
    $.get(
        "/offer/approve/accepted/"+id + "?notes=" + notes
    ).done(function (){
        window.location.href = "/offer/"+ id
    })
}

function rejectStatus(id){
    let notes = $("#notes").val();
    $.get(
        "/offer/approve/rejected/"+id + "?notes=" + notes
    ).done(function (){
        window.location.href = "/offer/"+ id
    })
}
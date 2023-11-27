$(document).ready(function(){
    let multipleCancelButton = new Choices('#interviewId', {
        removeItemButton: true,
        placeholderValue: "  Select interview"
    });
    $(".choices__inner").append('<i class="fa-solid fa-angle-down mt-2 me-1 float-end" style="font-size: 12px"></i>')
});

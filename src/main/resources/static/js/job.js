$(document).ready(function(){
   new Choices('#skills', {
        removeItemButton: true,
        placeholderValue: "Select skill",
       maxItemCount: 4
    });
     new Choices('#benefits', {
        removeItemButton: true,
        placeholderValue: "Select benefits"
    });
    $(".choices__inner").append('<i class="fa-solid fa-angle-down mt-2 me-1 float-end" style="font-size: 12px"></i>')
});
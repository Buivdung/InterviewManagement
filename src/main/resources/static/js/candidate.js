
$(document).ready(function () {
    new Choices('#skills', {
        removeItemButton: true,
        placeholderValue: "Select skill"
    });
    $(".choices__inner").append('<i class="fa-solid fa-angle-down mt-2 me-1 float-end" style="font-size: 12px"></i>')
    $("#cv").on("change", function () {
        let fileName = this.files[0].name;
        $("#fileName").text(fileName);
    });
});


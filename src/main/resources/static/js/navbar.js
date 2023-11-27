$(document).ready(function (){

})
 function click () {
    btn.addClass("list-group-item");
    $(this).removeClass("list-group-item");
}

let btn = $(".navbar-a").on("click",click);
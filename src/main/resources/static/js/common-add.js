let date = $(".date");
date.on("focusin", function (e) {
    e.target.type = "date"
})
date.on("focusout", function (e) {
    e.target.type = ""
})
let datetime = $(".datetime");
datetime.on("focusin", function (e) {
    e.target.type = "datetime-local"
})
datetime.on("focusout", function (e) {
    e.target.type = ""
})

// let formStatus = $(".form-select-status");
// let formRole = $(".form-select-role");
// let  labelStatus = $(".form-lb-status");
// let  labelRole = $(".form-lb-role");
// let formDepartment = $(".form-select-department");
// let formGender = $(".form-select-gender");
// let  labelDepartment = $(".form-lb-department");
// let  labelGender= $(".form-lb-gender");
// function changeLabel(element) {
//     element.css("top","0");
//     element.css("padding","0 2px");
//     element.css("background-color","white");
//     element.css("left","17px");
//     element.css("color","var(--blue2)");
// }
// formStatus.on("change",() =>changeLabel(labelStatus));
// formRole.on("change",() =>changeLabel(labelRole));
// formDepartment.on("change",() =>changeLabel(labelDepartment));
// formGender.on("change",() =>changeLabel(labelGender));

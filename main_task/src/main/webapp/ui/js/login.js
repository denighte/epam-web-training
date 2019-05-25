$(function () {
    let sign_up_tab = $(".signup-inactive");
    let sign_in_tab = $(".signin-active");
    $(".btn-tab").click(function () {
        $(".form-signin").toggleClass("form-signin-left");
        $(".form-signup").toggleClass("form-signup-left");
        $(".frame").toggleClass("frame-long");
        sign_up_tab.toggleClass("signup-active");
        sign_up_tab.toggleClass("signup-inactive");
        sign_in_tab.toggleClass("signin-inactive");
        sign_in_tab.toggleClass("signin-active");
        $(".forgot").toggleClass("forgot-left");
        $(this).removeClass("idle").addClass("active");
    });
});

$(function () {
    $(".btn-signup").click(function () {
        $(".nav").toggleClass("nav-up");
        $(".form-signup-left").toggleClass("form-signup-down");
        $(".success").toggleClass("success-left");
        $(".frame").toggleClass("frame-short");
        $("#name-check").attr("class", "checked");
    });
});

$(function () {
    $("#name-check").click(function () {
        $(".nav").toggleClass("nav-up");
        $(".form-signup-left").toggleClass("form-signup-down");
        $(".success").toggleClass("success-left");
        $(".frame").toggleClass("frame-short");
        $("#name-check").attr("class", "checked");
    });
});


$(function () {
    $(".btn-signin").click(function () {
        $(".btn-animate").toggleClass("btn-animate-grow");
        $(".welcome").toggleClass("welcome-left");
        $(".cover-photo").toggleClass("cover-photo-down");
        $(".frame").toggleClass("frame-short");
        $(".profile-photo").toggleClass("profile-photo-down");
        $(".btn-goback").toggleClass("btn-goback-up");
        $(".forgot").toggleClass("forgot-fade");
    });
});

$(function () {
    let toggle_silent = function(obj, cls) {
        obj.addClass("notransition");
        obj.toggleClass(cls);
        obj[0].offsetHeight;
        obj.removeClass("notransition");
    };
    $(".btn-goback").click(function () {
        toggle_silent($(".btn-animate"), "btn-animate-grow");
        toggle_silent($(".welcome"), "welcome-left");
        toggle_silent($(".btn-goback"), "btn-goback-up");
        $(".cover-photo").toggleClass("cover-photo-down");
        $(".frame").toggleClass("frame-short");
        $(".profile-photo").toggleClass("profile-photo-down");
        $(".forgot").toggleClass("forgot-fade");

    });
});



class Notification {
    constructor(jntf) {
        this.jntf = jntf;
        let self = this;
        jntf.find('.ns-close').click(function () {
            self.dismiss();
        });
    }

    show() {
        this.jntf.fadeIn();
    }

    dismiss() {
        this.jntf.fadeOut();
    }
}

$(function () {
   let notification = new Notification($(".ns-box"));
   $('p').click(function () {
       notification.show();
   });
    $('a').click(function () {
        notification.dismiss();
    });
});

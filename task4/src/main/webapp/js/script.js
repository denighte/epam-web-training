class SelectMenu {
    //elements must contain id
    constructor(elements) {
        this.selectMap = new Array();
        for(let i = 0; i < elements.length; ++i) {
            if(elements[i]) {
                this.selectMap[elements[i].id] = elements[i];
            }
        }
        this.currentId = elements[0].id;
        this.pattern = "<i class=\"fa fa-check\"></i>";
        this.previousHTML = elements[0].innerHTML;
        this._highlight(elements[0])
    }

    select (element) {
        this._unhighlight(this.selectMap[this.currentId]);
        this._highlight(element);
    }

    getSelected() {
        return this.selectMap[this.currentId];
    }

    _highlight (element) {
        this.previousHTML = element.innerHTML;
        this.currentId = element.id;
        element.innerHTML = element.innerHTML.concat(this.pattern)
    }

    _unhighlight (element) {
        element.innerHTML = this.previousHTML.slice();
    }
}

class Display {
    constructor(displayObject) {
        this.display = displayObject;
    }

    displayMessage(message, status) {
        this.display.style.color = "";
        switch (status) {
            case "ERROR":
                this.display.style.color = "#D8000C";
                break;
            case "OK":
                this.display.style.color = "#7FFF00";
                break;
            case "INFO":
                this.display.style.color = "";
        }
        this.display.innerHTML = message;
    }

    displayResponseMessage(message) {
        this.display.style.color = "";
        switch (message.status) {
            case "ERROR":
                this.display.style.color = "#D8000C";
                break;
            case "OK":
                this.display.style.color = "#50C32F";
                break;
            case "INFO":
                this.display.style.color = "";
        }
        if (message.messageKey && message.data) {
            this.display.innerHTML = message.messageKey + ", " + message.data;
        } else if (message.messageKey) {
            this.display.innerHTML = getLocalized(message.messageKey);
        } else if (message.data) {
            this.display.innerHTML = message.data;
        }
    }
}

function getLocalized(key) {
    return document.getElementById(key).value;
}
//on start init
let parserMenu = new SelectMenu([document.getElementById("DOM"), document.getElementById("SAX"), document.getElementById("StAX")]);
let localeMenu = new SelectMenu([document.getElementById("en"), document.getElementById("ru")]);
let display = new Display(document.getElementById("upload_visible"));

document.getElementById("parser_type").value = parserMenu.getSelected().id;
$.ajax({
    url: "locale",
    method: 'GET',
    type: 'GET', // For jQuery < 1.9
    success: function(data){
        localeMenu.select(document.getElementById(data))
    }
});

$("#upload-files").submit(function(e) {
    e.preventDefault(); // avoid to execute the actual submit of the form.
    let fileArray = $("#upload_hidden")[0].files;
    switch (fileArray.length) {
        case 0:
            display.displayMessage(getLocalized("upload_empty_error"), "ERROR");
            return;
        case 1:
            display.displayMessage(getLocalized("upload_few_error"), "ERROR");
            return;
        case 2:
            if(fileArray[0].name.endsWith(".xml") && fileArray[1].name.endsWith(".xsd")) {
                break;
            }
            if(fileArray[0].name.endsWith(".xsd") && fileArray[1].name.endsWith(".xsd")) {
                break;
            }
            display.displayMessage(getLocalized("upload_error"), "ERROR");
            return;
        default:
            display.displayMessage(getLocalized("upload_full_error"), "ERROR");
            return;
    }
    let form = $(this)[0];
    let data = new FormData(form);
    let url = $(this).attr('action');
    $.ajax({
        url: url,
        data: data, // serializes the form's elements.
        cache: false,
        contentType: false,
        processData: false,
        method: 'POST',
        type: 'POST', // For jQuery < 1.9
        success: function(resp){
            display.displayResponseMessage(resp);
        }
    });
});

$("#upload_hidden").change(function(e){
    let files = "";
    for(let i = 0; i < e.target.files.length; ++i) {
        files = files + "; " + e.target.files.item(i).name;
    }
    display.displayMessage(files.slice(2), "INFO")
});

$("#file-select").click(function () {
    document.getElementById('upload_hidden').click();
});
function selectParser(element) {
    parserMenu.select(element);
    document.getElementById("parser_type").value = element.id;
}
function selectLocale(element) {
    localeMenu.select(element);
    $.ajax({
        url: "locale",
        data: "locale=" + element.id ,
        method: 'POST',
        type: 'POST',
        success: function (data) {
            document.location.reload(true);
        }
    });
}

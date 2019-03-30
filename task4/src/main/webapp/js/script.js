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
        if (message.object.messageKey && message.object.data) {
            this.display.innerHTML = message.object.messageKey + ", " + message.object.data;
        } else if (message.messageKey) {
            this.display.innerHTML = getLocalized(message.object.messageKey);
        } else if (message.data) {
            this.display.innerHTML = message.object.data;
        }
    }
}

class Spinner {
    constructor() {
        this.pattern = document.createElement("div");
        this.pattern.id = "load_spinner";
        this.pattern.setAttribute("align", "center");
        this.pattern.innerHTML = '<i class="fa fa-3x fa-cog fa-spin"></i>';
    }

    display() {
        document.body.insertBefore(this.pattern, document.getElementById("table_holder"));
    }

    remove() {
        document.getElementById("load_spinner").remove();
    }
}

function getLocalized(key) {
    return document.getElementById(key).value;
}

function flatten(ob) {
    let flat = {};

    for (let i in ob) {
        if (!ob.hasOwnProperty(i)) continue;
        if ((typeof ob[i]) === 'object') {
            let flatObject = flatten(ob[i]);
            for (var x in flatObject) {
                if (!flatObject.hasOwnProperty(x)) continue;

                flat[i + '.' + x] = flatObject[x];
            }
        } else {
            flat[i] = ob[i];
        }
    }
    return flat;
};

class TableDisplay {
    constructor(holderDiv) {
        this.holder = holderDiv;
        this.data = [];
    }

    //objects array must have at least one element.
    display(objects) {
        this.holder.innerHTML = "";
        let keys = Object.keys(flatten(objects[0]));
        this.data.push('<table class="css-table" align="center">');
        this._displayHeader(keys);
        for(let i = 0; i < objects.length; ++i) {
            this.data.push("<tr>");
            let flat_object = flatten(objects[i]);
            for(let j = 0; j < keys.length; ++j) {
                this.data.push("<td>", flat_object[keys[j]], "</td>")
            }
            this.data.push("</tr>");
        }
        this.data.push('</table>');
        this.holder.innerHTML = this.data.join("");
        this.data.length = 0;
    }

    _displayHeader(header) {
        this.data.push("<tr>");
        for(let i = 0; i < header.length; ++i) {
            this.data.push("<th>", header[i], "</th>");
        }
        this.data.push("</tr>")
    }
}

$("#upload-files").submit(function(e) {
    e.preventDefault(); // avoid to execute the actual submit of the form.
    let fileArray = $("#upload_hidden")[0].files;
    if (!checkFilesNumber(fileArray)) {
        return;
    }
    let form = $(this)[0];
    let data = new FormData(form);
    let url = $(this).attr('action');
    spinner.display();
    $.ajax({
        url: url,
        data: data, // serializes the form's elements.
        cache: false,
        contentType: false,
        processData: false,
        method: 'POST',
        type: 'POST', // For jQuery < 1.9
        success: function(resp){
            handleResponse(resp);
        }
    });
});

function checkFilesNumber(fileArray) {
    switch (fileArray.length) {
        case 0:
            display.displayMessage(getLocalized("upload_empty_error"), "ERROR");
            return false;
        case 1:
            display.displayMessage(getLocalized("upload_few_error"), "ERROR");
            return false;
        case 2:
            if(fileArray[0].name.endsWith(".xml") && fileArray[1].name.endsWith(".xsd")) {
                return true;
            }
            if(fileArray[0].name.endsWith(".xsd") && fileArray[1].name.endsWith(".xsd")) {
                return true;
            }
            display.displayMessage(getLocalized("upload_error"), "ERROR");
            return false;
        default:
            display.displayMessage(getLocalized("upload_full_error"), "ERROR");
            return false;
    }
}

function handleResponse(resp) {
    spinner.remove();
    switch (resp.status) {
        case "TABLE":
            tableDisplay.display(resp.object);
            break;
        default:
            display.displayResponseMessage(resp);
            break;
    }
}

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



//on start init
let parserMenu = new SelectMenu([document.getElementById("DOM"), document.getElementById("SAX"), document.getElementById("StAX")]);
let localeMenu = new SelectMenu([document.getElementById("en"), document.getElementById("ru")]);
let display = new Display(document.getElementById("upload_visible"));
let tableDisplay = new TableDisplay(document.getElementById("table_holder"));
let spinner = new Spinner();

document.getElementById("parser_type").value = parserMenu.getSelected().id;
$.ajax({
    url: "locale",
    method: 'GET',
    type: 'GET', // For jQuery < 1.9
    success: function(data){
        localeMenu.select(document.getElementById(data))
    }
});
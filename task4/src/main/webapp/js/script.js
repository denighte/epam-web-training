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

$("#upload-file").submit(function(e) {
    e.preventDefault(); // avoid to execute the actual submit of the form.
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
        success: function(data){
            alert("file " + data + " was successfully loaded!");
        }
    });
});
var parserMenu = new SelectMenu([document.getElementById("1"), document.getElementById("2"), document.getElementById("3")]);
var localeMenu = new SelectMenu([document.getElementById("en"), document.getElementById("ru")]);
$.ajax({
    url: "locale",
    method: 'GET',
    type: 'GET', // For jQuery < 1.9
    success: function(data){
        localeMenu.select(document.getElementById(data))
    }
});

function selectParser(element) {
    parserMenu.select(element);
}
function selectLocale(element) {
    localeMenu.select(element);
    $.ajax({
        url: "locale",
        data: "locale=" + element.id ,
        method: 'POST',
        type: 'POST'
    });
    document.location.reload(true);
}
function getSelectedId() {
    return parserMenu.getSelected().id;
}

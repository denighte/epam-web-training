$(function () {
    let console_input = $('.console-input');
    let terminal = $(".terminal");
    let wrapper = $(".console-container");
    let body = $("body");

    // Output Welcome message
    output('Welcome to example console program.');
    output('');

// User Commands
    function echo (...a) {
        return a.join(' ')
    }
    echo.usage = "echo arg [arg ...]";
    echo.doc = "Echos to output whatever arguments are input";

    function ping() {
        //pong
        return '<span>pong</span></br><span class="pong"><b class="left">|</b><b class="right">|</b></span></br>';
    }
    ping.usage = "just type 'ping'";
    ping.doc = "pong";

    let THEMES_LIST = ['default', 'retro'];
    let CURRENT_THEME = 'console-style-default';

    function theme(param) {
        if (param) {
            let newThemePos = THEMES_LIST.indexOf(param);
            if (newThemePos !== -1) {
                let newTheme = 'console-style-' + THEMES_LIST[newThemePos];
                body.toggleClass(newTheme);
                body.toggleClass(CURRENT_THEME);
                CURRENT_THEME = newTheme;
                output("Style successfully changed.")
            } else {
                output("No such theme");
            }
        } else {
            output("Invalid parameter");
        }
    }
    theme.usage = "theme retro [default, retro]";
    theme.doc = "Changes styling of the terminal window.";

    function clear () {
        terminal.children().slice(0, -1).remove();
        console_input.val("");
    }
    clear.usage = "clear";
    clear.doc = "Clears the terminal screen";

    function help (cmd) {
        if (cmd) {
            let result = "";
            let usage = cmds[cmd].usage;
            let doc = cmds[cmd].doc;
            result += (typeof usage === 'function') ? usage() : usage;
            result += "\n";
            result += (typeof doc === 'function') ? doc() : doc;
            return result
        } else {
            let result = "**Commands:**\n\n";
            print = Object.keys(cmds);
            for (let p of print) {
                result += "- " + p + "\n"
            }
            return result
        }
    }
    help.usage = () => "help [command]";
    help.doc = () => "Without an argument, lists available commands. If used with an argument displays the usage & docs for the command.";

    var cmds = {
        echo,
        clear,
        help,
        ping,
        theme
    };

    /*
     * * * * * * * * USER INTERFACE * * * * * * *
     */

// Set Focus to Input
    wrapper.on('click', function (e) {
        console_input.focus();
    });

    function input() {
        let cmd = console_input.val();
        return cmd
    }

// Output to Console
    function output(print) {
        if (!window.md) {
            window.md = window.markdownit({
                html: true,
                linkify: true,
                breaks: true
            })
        }

        let location = $('.terminal > .console-form');
        if (console_input.val()) {
            let echo_message = $('<p class="prompt output"></p>');
            echo_message.text(console_input.val());
            location.before(echo_message);
            console_input.val("");
        }
        location.before(window.md.render(print));
        $('body').animate({
            scrollTop: $('.console-form').offset().top
        },1000);
    }

    let cmdHistory = [];
    let cursor = -1;

// Get User Command
    console_input.on('keydown', function(event) {
        if (event.which === 38) {
            // Up Arrow
            cursor = Math.min(++cursor, cmdHistory.length - 1);
            console_input.val(cmdHistory[cursor]);
        } else if (event.which === 40) {
            // Down Arrow
            cursor = Math.max(--cursor, -1);
            if (cursor === -1) {
                console_input.val('');
            } else {
                console_input.val(cmdHistory[cursor]);
            }
        } else if (event.which === 13) {
            event.preventDefault();
            cursor = -1;
            let text = input();
            let args = getTokens(text)[0];
            let cmd = args.shift().value;
            args = args.filter(x => x.type !== 'whitespace').map(x => x.value)
            cmdHistory.unshift(text);
            if (typeof cmds[cmd] === 'function') {
                let result = cmds[cmd](...args);
                if (result === void(0)) {
                    // output nothing
                } else if (result instanceof Promise) {
                    result.then(output)
                } else {
                    output(result)
                }
            } else if (cmd.trim() === '') {
                output('')
            } else {
                output("Command not found: `" + cmd + "`");
                output("Use 'help' for list of commands.")
            }
        }
    });
});


(function () {
    function getNumber (input) {
        let i = 0;
        while (i < input.length && '-.0123456789'.includes(input[i])) {
            i++
        }
        if (i === 0) return [[], input];
        let token = {
            type: 'number',
            value: Number(input.slice(0, i))
        };
        return [[token], input.slice(i)];
    }

    function getQuotedString (input) {
        if (input.length === 0) return [[], input];
        let i = 0;
        let q = input[i++];
        if (!"'\"".includes(q)) return [[], input];
        while (i < input.length) {
            if (input[i] === q) break;
            i++
        }
        let token = {
            type: 'string',
            value: input.slice(1, i)
        };
        return [[token], input.slice(i+1)]
    }

    function getWhitespace (input) {
        let i = 0;
        while (i < input.length && ' \t\n\r'.includes(input[i])) {
            i++;
        }
        if (i === 0) return [[], input];
        let token = {
            type: 'whitespace',
            value: input.slice(0, i)
        };
        return [[token], input.slice(i)]
    }

    function getRawString (input) {
        if (input.length === 0) return [[], input];
        let i = 0;
        while (i < input.length && !' \t\n\r'.includes(input[i])) {
            i++
        }
        if (i===0) return [[], input];
        let token = {
            type: 'word',
            value: input.slice(0, i)
        };
        return [[token], input.slice(i)]
    }

    function getTokens (input) {
        let parsers = [getNumber, getQuotedString, getRawString, getWhitespace]
        let result = [];
        let t;
        let lastsize = Infinity;
        while (input.length > 0 && input.length < lastsize) {
            lastsize = input.length;
            for (parser of parsers) {
                [t, input] = parser(input);
                result = [...result, ...t]
            }
        }
        return [result, input]
    }
    window.getTokens = getTokens
})();


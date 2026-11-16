const MAX_DELTA = 300;

let canvas;
let forwarding = false;
let wasLocked = false;
let initialLockClick = false;
let suppressReacquireClick = false;

const hint = document.createElement("div");
hint.textContent = "Click the game to capture the mouse · Esc releases it";
hint.style.cssText = [
    "position:fixed",
    "left:50%",
    "bottom:18px",
    "transform:translateX(-50%)",
    "z-index:10000",
    "padding:8px 12px",
    "border-radius:6px",
    "background:rgba(0,0,0,.78)",
    "color:white",
    "font:14px sans-serif",
    "pointer-events:none"
].join(";");
document.body.appendChild(hint);

function stop(event) {
    event.preventDefault();
    event.stopImmediatePropagation();
}

function center() {
    const bounds = canvas.getBoundingClientRect();
    return {
        x: bounds.left + bounds.width / 2,
        y: bounds.top + bounds.height / 2
    };
}

function forwardMouse(type, source, x, y) {
    forwarding = true;
    canvas.dispatchEvent(new MouseEvent(type, {
        bubbles: true,
        cancelable: true,
        view: window,
        clientX: x,
        clientY: y,
        screenX: x,
        screenY: y,
        button: source.button,
        buttons: source.buttons,
        detail: source.detail
    }));
    forwarding = false;
}

function requestLock() {
    if(typeof canvas.requestPointerLock !== "function")
    {
        hint.textContent = "Mouse capture requires Chrome, Firefox, Safari, or Zen";
        return false;
    }

    try
    {
        const result = canvas.requestPointerLock();
        if(result && typeof result.catch === "function")
            result.catch(error => {
                initialLockClick = false;
                hint.textContent = "Mouse capture failed: " + error.message;
            });
        return true;
    }
    catch(error)
    {
        initialLockClick = false;
        hint.textContent = "Mouse capture failed: " + error.message;
        return false;
    }
}

function handleMove(event) {
    if(forwarding || document.pointerLockElement !== canvas)
        return;

    stop(event);
    const origin = center();
    const deltaX = Math.max(-MAX_DELTA, Math.min(MAX_DELTA, event.movementX));
    const deltaY = Math.max(-MAX_DELTA, Math.min(MAX_DELTA, event.movementY));

    if(deltaX === 0 && deltaY === 0)
        return;

    forwardMouse("mousemove", event, origin.x + deltaX, origin.y + deltaY);
    forwardMouse("mousemove", event, origin.x, origin.y);
}

function handleButton(event) {
    if(forwarding)
        return;

    if(document.pointerLockElement === canvas)
    {
        if(initialLockClick && event.type === "mouseup")
        {
            initialLockClick = false;
            setTimeout(() => {
                const origin = center();
                forwardMouse("mousemove", new MouseEvent("mousemove"), origin.x, origin.y);
            }, 0);
            return;
        }

        if(suppressReacquireClick)
        {
            stop(event);
            return;
        }

        stop(event);
        const origin = center();
        forwardMouse(event.type, event, origin.x, origin.y);
        return;
    }

    if(event.type === "mousedown" && !wasLocked)
    {
        initialLockClick = requestLock();
    }
    else if(event.type === "mousedown" && wasLocked)
    {
        stop(event);
        suppressReacquireClick = true;
        requestLock();
    }
}

function handleClick(event) {
    if(forwarding)
        return;

    if(suppressReacquireClick)
    {
        stop(event);
        suppressReacquireClick = false;
        return;
    }
}

function install(candidate) {
    if(canvas === candidate)
        return;

    canvas = candidate;
    canvas.addEventListener("mousemove", handleMove, true);
    canvas.addEventListener("mousedown", handleButton, true);
    canvas.addEventListener("mouseup", handleButton, true);
    canvas.addEventListener("click", handleClick, true);
}

function findCanvas() {
    const candidate = document.querySelector("#noVNC_container canvas");
    if(candidate)
        install(candidate);
}

document.addEventListener("pointerlockchange", () => {
    const locked = document.pointerLockElement === canvas;
    hint.style.display = locked ? "none" : "block";

    if(locked)
    {
        wasLocked = true;
        if(!initialLockClick)
        {
            const origin = center();
            forwardMouse("mousemove", new MouseEvent("mousemove"), origin.x, origin.y);
        }
    }
});

new MutationObserver(findCanvas).observe(document.documentElement, {
    childList: true,
    subtree: true
});
findCanvas();

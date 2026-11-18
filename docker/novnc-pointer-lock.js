import UI from "./ui.js";

// The Java listener measures VNC coordinates from the desktop center.
// Keep each VNC move inside the game panel and drain large mouse flicks
// over several frames instead of dropping the excess movement.
const MAX_STEP = 100;
const RECENTER_DELAY_MS = 8;

let canvas;
let wasLocked = false;
let initialLockClick = false;
let suppressReacquireClick = false;
let pendingX = 0;
let pendingY = 0;
let moveFrame = null;
let recenterTimer = null;

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

function sendMove(deltaX = 0, deltaY = 0) {
    if(!UI.rfb || !canvas.width || !canvas.height)
        return;
    const bounds = canvas.getBoundingClientRect();
    // Firefox pins synthetic DOM mouse events to the lock point. Send the
    // position through noVNC's VNC input handler instead.
    UI.rfb._handleMouseMove(
        bounds.width / 2 + deltaX * bounds.width / canvas.width,
        bounds.height / 2 + deltaY * bounds.height / canvas.height);
}

function clearMovement() {
    pendingX = 0;
    pendingY = 0;
    if(moveFrame !== null)
        cancelAnimationFrame(moveFrame);
    if(recenterTimer !== null)
        clearTimeout(recenterTimer);
    moveFrame = null;
    recenterTimer = null;
}

function sendButton(event) {
    if(!UI.rfb)
        return;
    const bounds = canvas.getBoundingClientRect();
    UI.rfb._handleMouseButton(bounds.width / 2, bounds.height / 2,
        event.type === "mousedown", 1 << event.button);
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
    if(document.pointerLockElement !== canvas)
        return;

    stop(event);
    pendingX += event.movementX;
    pendingY += event.movementY;
    if(moveFrame === null && recenterTimer === null)
        moveFrame = requestAnimationFrame(flushMovement);
}

function flushMovement() {
    moveFrame = null;
    if(document.pointerLockElement !== canvas || !canvas.width || !canvas.height)
        return;

    const deltaX = Math.max(-MAX_STEP, Math.min(MAX_STEP, pendingX));
    const deltaY = Math.max(-MAX_STEP, Math.min(MAX_STEP, pendingY));
    if(deltaX === 0 && deltaY === 0)
        return;

    pendingX -= deltaX;
    pendingY -= deltaY;
    sendMove(deltaX, deltaY);

    recenterTimer = setTimeout(() => {
        recenterTimer = null;
        if(document.pointerLockElement !== canvas)
            return;
        sendMove();
        if(pendingX !== 0 || pendingY !== 0)
            moveFrame = requestAnimationFrame(flushMovement);
    }, RECENTER_DELAY_MS);
}

function handleButton(event) {
    if(document.pointerLockElement === canvas)
    {
        if(initialLockClick && event.type === "mouseup")
        {
            initialLockClick = false;
            setTimeout(() => sendMove(), 0);
            return;
        }

        if(suppressReacquireClick)
        {
            stop(event);
            return;
        }

        stop(event);
        sendButton(event);
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
            sendMove();
    }
    else
    {
        clearMovement();
        initialLockClick = false;
    }
});

document.addEventListener("keydown", event => {
    if(event.key === "Escape" && document.pointerLockElement === canvas)
        document.exitPointerLock();
}, true);

new MutationObserver(findCanvas).observe(document.documentElement, {
    childList: true,
    subtree: true
});
findCanvas();

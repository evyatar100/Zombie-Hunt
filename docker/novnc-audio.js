const SOUND_FILES = [
    "Ammo.wav",
    "GunFire.wav",
    "GunPunch.wav",
    "Punch.wav",
    "Reload.wav",
    "Siran.wav",
    "Thunder.wav",
    ...Array.from({ length: 7 }, (_, index) => `Back_Sounds/Sound${index}.wav`)
];
const allowedSounds = new Set(SOUND_FILES);
const buffers = new Map();
const AudioContextClass = window.AudioContext || window.webkitAudioContext;
const audioContext = AudioContextClass ? new AudioContextClass() : null;

let lastSequence = null;
let cachedCount = 0;

function loadSound(file) {
    if(!buffers.has(file))
    {
        buffers.set(file, fetch(`sounds/${file}`)
            .then(response => {
                if(!response.ok)
                    throw new Error(`Could not load ${file}: ${response.status}`);
                return response.arrayBuffer();
            })
            .then(data => audioContext.decodeAudioData(data))
            .then(buffer => {
                document.documentElement.dataset.zombieAudioCached = String(++cachedCount);
                return buffer;
            }));
    }
    return buffers.get(file);
}

async function unlockAudio() {
    if(!audioContext)
        return;
    await audioContext.resume();
    document.documentElement.dataset.zombieAudioState = audioContext.state;
}

async function playSound(file) {
    if(!allowedSounds.has(file) || audioContext?.state !== "running")
        return;

    try
    {
        const buffer = await loadSound(file);
        const source = audioContext.createBufferSource();
        source.buffer = buffer;
        source.connect(audioContext.destination);
        source.start();
        document.documentElement.dataset.zombieAudioLastPlayed = file;
    }
    catch(error)
    {
        console.warn("Zombie Hunt audio:", error);
    }
}

async function pollSounds() {
    try
    {
        const response = await fetch("app/zombie-hunt-audio-events.txt", { cache: "no-store" });
        if(!response.ok)
            throw new Error(`Audio events: ${response.status}`);

        const events = (await response.text()).trim().split("\n")
            .map(line => {
                const match = /^(\d+)\t([A-Za-z0-9_/]+\.wav)$/.exec(line);
                return match ? { sequence: Number(match[1]), file: match[2] } : null;
            })
            .filter(Boolean);
        const newest = events.at(-1)?.sequence ?? 0;

        if(lastSequence === null)
            lastSequence = newest;
        else
        {
            if(newest < lastSequence)
                lastSequence = 0; // The game container restarted.
            for(const event of events)
                if(event.sequence > lastSequence)
                    playSound(event.file);
            lastSequence = newest;
        }
    }
    catch(error)
    {
        // The container may be starting or reconnecting; try the next poll.
    }
    finally
    {
        setTimeout(pollSounds, 100);
    }
}

document.addEventListener("pointerdown", unlockAudio, true);
document.addEventListener("keydown", unlockAudio, true);
for(const file of SOUND_FILES)
    loadSound(file).catch(error => console.warn(error));
pollSounds();

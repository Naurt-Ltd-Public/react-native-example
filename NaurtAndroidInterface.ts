export interface NaurtAndroidInterface {
    getName(): String;
    getConstants(): Map<String, String>;
    initialiseNaurt(apiKey: String): void;
    resumeNaurt(): void;
    pauseNaurt(): void;
    startNaurt(): void;
    stopNaurt(): void;
    isNaurtInitialised(): boolean;
    addListener(eventName: String): void;
    removeListeners(count: number): void;
}

export interface NaurtInisialiedEvent extends Event {
    isInitialised: boolean
}

export interface NaurtValidatedEvent extends Event {
    isValidated: boolean
}

export interface NaurtRunningEvent extends Event {
    isRunning: boolean
}

export interface NaurtPointEvent extends Event {
    latitude: number,
    longitude: number,
    timestamp: String
}
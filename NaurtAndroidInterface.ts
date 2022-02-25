export interface NaurtAndroidInterface {
    getName(): String;
    getConstants(): Map<String, String>;
    initialiseNaurt(apiKey: String): void;
    resumeNaurt(): void;
    pauseNaurt(): void;
    startNaurt(callback: (eventId: number, latitude: number, longitude: number, timestamp: number) => void): void;
    stopNaurt(): void;
    isNaurtInitialised(): boolean;
}
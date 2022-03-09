export interface NaurtIosInterface {
    initialiseNaurt(apiKey: String, precision: number): void;
    resumeNaurt(): void;
    pauseNaurt(): void;
    startNaurt(): void;
    stopNaurt(): void;
}
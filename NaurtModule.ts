import { NativeModules } from 'react-native';
const { NaurtModule } = NativeModules;

interface NaurtInterface {
    getName(): String;
    getConstants(): Map<String, String>;
    initialiseNaurt(apiKey: String): void;
    resumeNaurt(): void;
    pauseNaurt(): void;
    startNaurt(callback: (eventId: Number, latitude: Number, longitude: Number, timestamp: Number) => void): void;
    stopNaurt(): void;
    checkPermissions(): boolean;
}

export default NaurtModule as NaurtInterface;
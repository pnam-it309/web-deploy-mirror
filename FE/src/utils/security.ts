/**
 * Security utilities for masking and obfuscating sensitive information like IDs
 */

const SALT = 'fpl-udpm-catalog';
const SHORT_ID_LENGTH = 18;

/**
 * ID Mapping Storage
 * Layer 2: Maps Short 18-char IDs to Encoded Long IDs
 */
const getMappingStore = (): Record<string, string> => {
    try {
        const stored = sessionStorage.getItem('id_mapping_registry');
        return stored ? JSON.parse(stored) : {};
    } catch (e) {
        return {};
    }
};

const saveMappingStore = (store: Record<string, string>) => {
    try {
        sessionStorage.setItem('id_mapping_registry', JSON.stringify(store));
    } catch (e) { }
};

/**
 * Generates a random alphanumeric string of a specific length
 */
const generateRandomCode = (length: number): string => {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
};

/**
 * Lớp 1: Mã hóa ID gốc sang chuỗi Base64 bảo mật
 */
const encryptLayer1 = (id: string): string => {
    return btoa(`${SALT}:${id}`).replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_');
};

/**
 * Lớp 1: Giải mã chuỗi Base64 về ID gốc
 */
const decryptLayer1 = (encoded: string): string => {
    try {
        let base64 = encoded.replace(/-/g, '+').replace(/_/g, '/');
        while (base64.length % 4) base64 += '=';
        const decoded = atob(base64);
        return decoded.startsWith(`${SALT}:`) ? decoded.split(`${SALT}:`)[1] : decoded;
    } catch (e) {
        return encoded;
    }
};

/**
 * Lớp 2: Chuyển chuỗi mã hóa dài sang mã 18 ký tự ngẫu nhiên
 * @returns 18-char code
 */
export const encodeId = (id: string | number | undefined | null): string => {
    if (id === undefined || id === null) return '';
    const realId = id.toString();
    const longEncoded = encryptLayer1(realId);

    const store = getMappingStore();

    // Tìm xem ID này đã có mã 18 ký tự chưa
    const existingShortId = Object.keys(store).find(key => store[key] === longEncoded);
    if (existingShortId) return existingShortId;

    // Nếu chưa có, tạo mã mới
    const newShortId = generateRandomCode(SHORT_ID_LENGTH);
    store[newShortId] = longEncoded;
    saveMappingStore(store);

    return newShortId;
};

/**
 * Giải mã từ mã 18 ký tự về ID gốc
 */
export const decodeId = (shortId: string | undefined | null): string => {
    if (!shortId) return '';

    // Nếu độ dài không phải 18, có thể là mã cũ hoặc ID thật
    if (shortId.length !== SHORT_ID_LENGTH) {
        return decryptLayer1(shortId);
    }

    const store = getMappingStore();
    const longEncoded = store[shortId];

    if (longEncoded) {
        return decryptLayer1(longEncoded);
    }

    // Fallback nếu không tìm thấy trong mapping (có thể do clear cache)
    return decryptLayer1(shortId);
};

/**
 * Mask ID for display
 */
export const maskId = (id: string | number | undefined | null, visibleChars = 4): string => {
    if (!id) return '';
    const str = id.toString();
    if (str.length <= visibleChars * 2) return str;
    return `${str.substring(0, visibleChars)}...${str.substring(str.length - visibleChars)}`;
};

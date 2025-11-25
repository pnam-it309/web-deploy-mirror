export function toSlug(str: string): string {
  if (!str) return '';

  return String(str)
    .normalize('NFKD') // Tách các ký tự dấu (ví dụ: 'ê' -> 'e' + '´')
    .replace(/[\u0300-\u036f]/g, '') // Xoá tất cả các ký tự dấu
    .trim() // Xoá khoảng trắng đầu/cuối
    .toLowerCase() // Chuyển thành chữ thường
    .replace(/[^a-z0-9 -]/g, '') // Xoá các ký tự không phải (chữ, số, gạch ngang, khoảng trắng)
    .replace(/\s+/g, '-') // Thay khoảng trắng bằng gạch ngang
    .replace(/-+/g, '-'); // Xoá các gạch ngang thừa
}
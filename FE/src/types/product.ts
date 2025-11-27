export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  image: string;
  category: string;
  rating: number;
  reviewCount?: number;
  badge?: 'Sale' | 'New' | 'Limited' | 'Best Seller';
  inStock?: boolean;
  images?: string[];
  details?: {
    [key: string]: string;
  };
}

export interface CartItem {
  product: Product;
  quantity: number;
}

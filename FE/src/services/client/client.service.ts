import { APIService } from "@/services/api.service";

// const api = new APIService();

export interface PublicDomain {
    id: string;
    name: string;
    slug: string;
    icon: string;
    productCount?: number;
}

export interface PublicProduct {
    id: string;
    name: string;
    description: string;
    thumbnail: string;
    domainName: string;
    domainId: string;
    technologies: { name: string; icon: string }[];
}

export interface ProductDetail extends PublicProduct {
    detailDescription: string;
    website?: string;
    sourceCode?: string;
    media: { type: 'image' | 'video'; url: string }[];
    team: { name: string; studentCode: string; role: string; avatar?: string; isLeader?: boolean }[];
    features: { name: string; description: string }[];
}

export const getPublicDomains = async (): Promise<PublicDomain[]> => {
    // START MOCK DATA
    console.log("Fetching public domains (MOCK)");
    return [
        { id: '1', name: 'Web Development', slug: 'web-dev', icon: 'https://cdn-icons-png.flaticon.com/512/1005/1005141.png', productCount: 12 },
        { id: '2', name: 'Mobile App', slug: 'mobile-app', icon: 'https://cdn-icons-png.flaticon.com/512/2586/2586488.png', productCount: 8 },
        { id: '3', name: 'AI/ML', slug: 'ai-ml', icon: 'https://cdn-icons-png.flaticon.com/512/1693/1693746.png', productCount: 5 },
        { id: '4', name: 'IoT', slug: 'iot', icon: 'https://cdn-icons-png.flaticon.com/512/3413/3413535.png', productCount: 3 },
    ];
    // END MOCK DATA
};

export const getPublicFeaturedProducts = async (): Promise<PublicProduct[]> => {
     // START MOCK DATA
    console.log("Fetching featured products (MOCK)");
    return [
        {
            id: '101',
            name: 'E-Commerce Platform',
            description: 'A full-stack e-commerce solution with React and Node.js. Includes payment gateway integration and admin dashboard.',
            thumbnail: 'https://images.unsplash.com/photo-1557821552-17105176677c?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
            domainName: 'Web Development',
            domainId: '1',
            technologies: [
                { name: 'React', icon: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg' },
                { name: 'NodeJS', icon: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/nodejs/nodejs-original.svg' },
                { name: 'MongoDB', icon: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mongodb/mongodb-original.svg' }
            ]
        },
        {
            id: '102',
            name: 'Smart Home Hub',
            description: 'IoT system for controlling home appliances layout via mobile app. Uses MQTT protocol for real-time communication.',
            thumbnail: 'https://images.unsplash.com/photo-1558002038-109177381795?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
            domainName: 'IoT',
            domainId: '4',
             technologies: [
                { name: 'Flutter', icon: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/flutter/flutter-original.svg' },
                { name: 'Firebase', icon: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/firebase/firebase-plain.svg' }
            ]
        },
         {
            id: '103',
            name: 'Face Recognition Attendance',
            description: 'Automated attendance system using Python and OpenCV. High accuracy face detection and recognition.',
            thumbnail: 'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
            domainName: 'AI/ML',
            domainId: '3',
             technologies: [
                { name: 'Python', icon: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/python/python-original.svg' },
                { name: 'TensorFlow', icon: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/tensorflow/tensorflow-original.svg' }
            ]
        }
    ];
    // END MOCK DATA
}

export const getProductDetail = async (id: string): Promise<ProductDetail | null> => {
    // START MOCK DATA
    console.log(`Fetching product detail for ${id} (MOCK)`);
    await new Promise(r => setTimeout(r, 500)); // Simulate delay
    
    return {
        id: id,
        name: 'E-Commerce Platform',
        description: 'A full-stack e-commerce solution...',
        detailDescription: 'This project aims to provide a comprehensive solution for small businesses to go online. It includes a customer-facing storefront, an admin dashboard for inventory management, and integration with Stripe for payments.',
        thumbnail: 'https://images.unsplash.com/photo-1557821552-17105176677c?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80',
        domainName: 'Web Development',
        domainId: '1',
        technologies: [
            { name: 'React', icon: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg' },
            { name: 'NodeJS', icon: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/nodejs/nodejs-original.svg' },
             { name: 'MongoDB', icon: 'https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mongodb/mongodb-original.svg' }
        ],
        website: 'https://example.com',
        sourceCode: 'https://github.com/example/project',
        media: [
            { type: 'image', url: 'https://images.unsplash.com/photo-1557821552-17105176677c?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80' },
            { type: 'image', url: 'https://images.unsplash.com/photo-1563013544-824ae1b704d3?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80' },
            // { type: 'video', url: 'https://www.youtube.com/embed/dQw4w9WgXcQ' }
        ],
        team: [
            { name: 'Nguyen Van A', studentCode: 'PH12345', role: 'Backend Developer', isLeader: true },
            { name: 'Tran Thi B', studentCode: 'PH12346', role: 'Frontend Developer' },
            { name: 'Le Van C', studentCode: 'PH12347', role: 'Tester' }
        ],
        features: [
            { name: 'Product Management', description: 'Create, update, delete products with images and variants.' },
            { name: 'Order Processing', description: 'Manage orders, update status, and print invoices.' },
            { name: 'Customer Accounts', description: 'Registration, login, and profile management.' }
        ]
    };
    // END MOCK DATA
};

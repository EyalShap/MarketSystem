import { createContext } from "react";
import StoreModel from "./models/StoreModel"
import RestResponse from "./models/RestResponse";
import ProductModel from "./models/ProductModel";
import Permission from "./models/Permission";
import MemberModel from "./models/MemberModel";
import { registerModel } from "./models/registerModel";
import cartModel from "./models/CartModel";
import { get } from "http";
import ProductCartModel from "./models/ProductCartModel";
import { OrderModel } from "./models/OrderModel"; // Adjust the path as needed
import axios from "axios";

export const login = async(username: string, password: string) => {
    ///request REST to login...
    const response=(await axios.post('http://127.0.0.1:8080/api/user/login',{username: username, password: password},{headers:{
        'Content-Type': 'application/json',
        //Authorization: `Bearer ${jwt_token}`
      }})).data
    return response;
}
export const loginUsingJwt = async(username: string, jwt: string) => {
    ///request REST to login...
    const response=(await axios.post(`http://127.0.0.1:8080/api/user/loginUsingJwt?username=${username}`,{},{headers:{
        'Content-Type': 'application/json',
        Authorization: `Bearer ${jwt}`
      }})).data
    return response;
}
export const enterAsGuest = async() => {
    ///request REST to login...
    const response=(await axios.post('http://127.0.0.1:8080/api/user/enterAsGuest')).data
    console.log(response);
    return Number.parseInt(response.dataJson);
}
export const registerMember = async(registerModel: registerModel) => {
    ///request REST to login...
    const response = await (await axios.post(
        'http://127.0.0.1:8080/api/user/register',
        registerModel,
        {
            headers: {
                'Content-Type': 'application/json',
                // Authorization: `Bearer ${jwt_token}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response;
}

export const getStoreInfo = (storeId: string): StoreModel => {
    let defaultExample: StoreModel = {founderUsername: "mrOwnerMan", storeId: 0, storeName: "TestStore", email: "coolio@gmail.com", address: "Be'er Sheva, Reger Street 78", rank: 3, phoneNumber: "052-0520520", closingHours: 0, openingHours: 0}
    return defaultExample;
}

export const getStoreProducts = (storeId: string): ProductModel[] => {
    let defaultExample1: ProductModel = {id:1,storeId:2,name: "Example1", price: 123.3}
    let defaultExample2: ProductModel = {id:1,storeId:2,name: "Example2", price: 3.5}
    let list: ProductModel[] = []
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    return list;
}

export const searchAndFilterStoreProducts = (storeId: string, category: string, keywords: string, minprice: number, maxprice: number): ProductModel[] => {
    let defaultExample1: ProductModel = {id:1,storeId:2,name: "Example1", price: 123.3}
    let defaultExample2: ProductModel = {id:1,storeId:2,name: "Example2", price: 3.5}
    let list: ProductModel[] = []
    if(keywords === "Example1"){
        console.log("AAAAAAAAAAAAAA");
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
    }else if(keywords === "Example2"){
        list.push(defaultExample2)
        list.push(defaultExample2)
        list.push(defaultExample2)
        list.push(defaultExample2)
    }else{
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
    }
    
    return list;
}

export const isOwner = (storeId: string): boolean => {
    return true;
}

export const isManager = (storeId: string): boolean => {
    return true;
}

export const hasPermission = (storeId: string, permission: Permission): boolean => {
    return true;
}

export const storeActive = (storeId: string): boolean => {
    return true;
}

export const getPermissions = (storeId: string): Permission[] => {
    return [Permission.ADD_PRODUCTS, Permission.DELETE_PRODUCTS, Permission.UPDATE_PRODUCTS, Permission.ADD_BUY_POLICY, Permission.ADD_DISCOUNT_POLICY, Permission.ADD_MANAGER, Permission.ADD_OWNER, Permission.CLOSE_STORE, Permission.REOPEN_STORE];
}

export const getStoreManagers = (storeId: string): MemberModel[] => {
    let defaultExample1: MemberModel = {username: "Eric", firstName: "Eric", lastName: "Einstein", email: "eric@excited.com", phoneNumber: "052-0520525",birthday: "2024-06-06"}
    let defaultExample2: MemberModel = {username: "Benny", firstName: "Benny", lastName: "Bobby", email: "benny@sad.com", phoneNumber: "052-0520525",birthday: "2024-06-06"}
    let list: MemberModel[] = []
    list.push(defaultExample1)
    list.push(defaultExample2)
    return list;
}

export const getStoreOwners = (storeId: string): MemberModel[] => {
    let defaultExample1: MemberModel = {username: "mrOwnerMan", firstName: "Owner", lastName: "Man", email: "man@store.com", phoneNumber: "052-0520525",birthday: "2024-06-06"}
    let defaultExample2: MemberModel = {username: "GuyWhoOwnsStore", firstName: "Guy", lastName: "Store", email: "guy@store.com", phoneNumber: "052-0520525",birthday: "2024-06-06"}
    let list: MemberModel[] = []
    list.push(defaultExample1)
    list.push(defaultExample2)
    return list;
}
export const getMember = async(username: string): Promise<MemberModel> => {
    const profile= {
        username: 'idanasis',
        firstName: 'Idan',
        lastName: 'Asis',
        email: 'idanasis86@gmail.com',
        phoneNumber: '0523072999',
        birthday: '2024-06-06' // Default value for birthday
      }
    return profile;
}
export const viewCart = (username:string): cartModel => {
    const cart: ProductCartModel[] = [
        { id: 1, amount: 5,storeId:1, originalPrice: 100, discountedPrice: 90, name: 'example1'  },
        { id: 2, amount: 3,storeId:1, originalPrice: 50, discountedPrice: 45, name: 'example2'}
    ];
    let totalPrice = 150;
    let totalDiscountedPrice = 135;
    return {
        products: cart,
        totalPrice: totalPrice,
        discountedPrice: totalDiscountedPrice
    };
}

export const getProductDetails = (productId: number): ProductModel => {
    return {id:2,storeId:4, name: "Example1", price: 123.3 };
}
export const updateCart = (cart: cartModel) => {
    console.log("Cart updated");
}
export const removeFromCart = (cart: cartModel) => {
    console.log("Cart updated");
}

// export const searchProducts = async(username: string,productName: string, productCategory: string, minProductPrice: number, maxProductPrice: number) => {
 
//     const response=(await axios.post('http://127.0.0.1:8080/api/product/getFilteredProducts',{productName, productCategory, minProductPrice, maxProductPrice},{headers:{
//         'Content-Type': 'application/json',
//         //Authorization: `Bearer ${jwt_token}`
//       }})).data
//     return response;

       
// };

export const searchProducts = async (
    username: string,
    productName: string,
    productCategory: string,
    minProductPrice: number,
    maxProductPrice: number
): Promise<ProductModel[]> => {
    console.log("hi");

    const response = (await axios.get('http://127.0.0.1:8080/api/product/getFilteredProducts', {
        headers: {
            'Content-Type': 'application/json',
            // Authorization: `Bearer ${jwt_token}`
        },
        params: {
            productName,
            productCategory,
            minProductPrice,
            maxProductPrice
        }
    })).data;

    // Transform the response into ProductModel[]
    const products: ProductModel[] = response.map((product: any) => ({
        id: product.productID,
        name: product.productName,
        storeId: product.productID, // Assuming storeId is same as productID
        price: product.productPrice,
        productCategory: product.productCategory,
        productDescription: product.description,
        productRank: product.productRank
    }));

    return products;
};


export const getOrders = async (username: string): Promise<OrderModel[]> => {
    return [
        {
            id: '27cba69d-4c3d-4098-b42d-ac7fa62b7664',
            date: 'August 12',
            total: 35.06,
            products: [
                {
                    id: 1,
                    name: 'Black and Gray Athletic Cotton Socks - 6 Pairs',
                    storeId: 1,
                    quantity: 1
                },
                {
                    id: 2,
                    name: 'Adults Plain Cotton T-Shirt - 2 Pack',
                    storeId: 1,
                    quantity: 2
                }
            ]
        },
        {
            id: 'b6b6c212-d30e-4d4a-805d-90b52ce6b37d',
            date: 'June 10',
            total: 41.90,
            products: [
                {
                    id: 3,
                    name: 'Intermediate Size Basketball',
                    storeId: 2,
                    quantity: 2
                }
            ]
        }
    ];
};

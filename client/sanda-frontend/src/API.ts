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
import RoleModel from "./models/RoleModel";
import CreateStoreModel from "./models/CreateStoreModel";
import { NotificationModel } from "./models/NotificationModel";
import { ProductOrderModel } from "./models/ProductOrderModel";


const server: string = 'http://127.0.0.1:8080'; 

export const login = async(username: string, password: string) => {
    ///request REST to login...
    const response=(await axios.post(`${server}/api/user/login`,{username: username, password: password},{headers:{
        'Content-Type': 'application/json',
        //Authorization: `Bearer ${jwt_token}`
      }})).data
    return response;
}
export const loginFromGuest = async(username: string, password: string,guestId: number ) => {
    ///request REST to login...
    const response=(await axios.post(`${server}/api/user/loginFromGuest`,{username: username, password: password, guestId:guestId},{headers:{
        'Content-Type': 'application/json',
        //Authorization: `Bearer ${jwt_token}`
      }})).data
    return response;
}
export const loginUsingJwt = async(username: string, jwt: string) => {
    ///request REST to login...
    const response=(await axios.post(`${server}/api/user/loginUsingJwt?username=${username}`,{},{headers:{
        'Content-Type': 'application/json',
        Authorization: `Bearer ${jwt}`
      }})).data
    return response;
}
export const logout = async(username: string) => {
    ///request REST to login...
    const response=(await axios.post(`${server}/api/user/logout`,{field: username},{headers:{
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }})).data
      return Number.parseInt(response.dataJson);
}
export const enterAsGuest = async() => {
    ///request REST to login...
    const response=(await axios.post(`${server}/api/user/enterAsGuest`)).data
    console.log(response);
    return Number.parseInt(response.dataJson);
}
export const registerMember = async(registerModel: registerModel) => {
    ///request REST to login...
    const response = await (await axios.post(
        `${server}/api/user/register`,
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
    let defaultExample1: MemberModel = {username: "Eric", firstName: "Eric", lastName: "Einstein", emailAddress: "eric@excited.com", phoneNumber: "052-0520525",birthDate: "2024-06-06"}
    let defaultExample2: MemberModel = {username: "Benny", firstName: "Benny", lastName: "Bobby", emailAddress: "benny@sad.com", phoneNumber: "052-0520525",birthDate: "2024-06-06"}
    let list: MemberModel[] = []
    list.push(defaultExample1)
    list.push(defaultExample2)
    return list;
}

export const getStoreOwners = (storeId: string): MemberModel[] => {
    let defaultExample1: MemberModel = {username: "mrOwnerMan", firstName: "Owner", lastName: "Man", emailAddress: "man@store.com", phoneNumber: "052-0520525",birthDate: "2024-06-06"}
    let defaultExample2: MemberModel = {username: "GuyWhoOwnsStore", firstName: "Guy", lastName: "Store", emailAddress: "guy@store.com", phoneNumber: "052-0520525",birthDate: "2024-06-06"}
    let list: MemberModel[] = []
    list.push(defaultExample1)
    list.push(defaultExample2)
    return list;
}
export const getMember = async(username: string): Promise<MemberModel> => {
      const response = await fetch(
        `${server}/api/user/getUserDTO?username=${username}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data = await response.json();
    
    // Assuming the API returns the data in dataJson
    const profileData = JSON.parse(data.dataJson) as MemberModel;
    console.log(profileData);
    // Validate the structure of profileData before returning

    return profileData;
}

export const viewMemberCart = async(username:string): Promise<string> => {
    const response= await fetch(`${server}/api/user/getCart?username=${username}`,{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    })
    const data= await response.json();
    return data;
}

export const viewGuestCart = async(guestId:number): Promise<string> => {
const response= await fetch(`${server}/api/guest/getCart?guestId=${guestId}`);
const data= await response.json();
return data;
}

export const getProductDetails = (productId: number): ProductModel => {
    return {id:2,storeId:4, name: "Example1", price: 123.3 };
}
export const removeFromCart = (cart: cartModel) => {
    console.log("Cart updated");
}
export const updateFirstName = async(firstName:string) => {
    const response = await (await axios.patch(
        `${server}/api/user/setFirstName?username=${localStorage.getItem("username")}`,
        {field: firstName},
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response;
}
export const updateLastName = async (lastName:string) => {
    const response = await (await axios.patch(
        `${server}/api/user/setLastName?username=${localStorage.getItem("username")}`,
        {field: lastName},
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response;
}
export const updateEmail = async(email:string) => {
    const response = await (await axios.patch(
        `${server}/api/user/setEmailAddress?username=${localStorage.getItem("username")}`,
        {field: email},
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response
}
export const updatePhone = async(phone:string) => {
    const response = await (await axios.patch(
        `${server}/api/user/setPhoneNumber?username=${localStorage.getItem("username")}`,
        {field: phone},
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response
}
export const updateBirthday = async(birthday:string) => {
    const response = await (await axios.patch(
        `${server}/api/user/setBirthday?username=${localStorage.getItem("username")}`,
        {field: birthday},
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response
}

export const searchProducts = async (
    username: string,
    productName: string,
    productCategory: string,
    minProductPrice: number,
    maxProductPrice: number,
    minProductRank: number
): Promise<ProductModel[]> => {
    const server = 'http://127.0.0.1:8080'; 

    const response = (await axios.get(`${server}/api/product/getFilteredProducts`, {
        headers: {
            'Content-Type': 'application/json',
            // Authorization: `Bearer ${jwt_token}`
        },
        params: {
            productName,
            productCategory,
            minProductPrice,
            maxProductPrice,
            minProductRank
        }
    })).data;

    const dataJson = JSON.parse(response.dataJson);

    const products: ProductModel[] = dataJson.map((product: any) => ({
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
    try {
        const response = (await axios.get(`${server}/api/getOrderHistory`, {
            headers: {
                'Content-Type': 'application/json',
                // Add Authorization header if required
                // Authorization: `Bearer ${jwt_token}`
            },
            params: {
                username: username
            }
        })).data;

        // Parse the JSON response
        const ordersData: { [key: number]: ProductDataPrice[] } = JSON.parse(response.dataJson);
        const orders: OrderModel[] = Object.entries(ordersData).map(([orderId, products], index) => {
            const total = products.reduce((acc, product) => acc + product.newPrice * product.amount, 0);
            const orderProducts: ProductOrderModel[] = products.map(product => ({
                id: product.id,
                name: product.name,
                quantity: product.amount,
                storeId: product.storeId
            }));
            return {
                id: orderId,
                date: `Date ${index + 1}`, // Adjust this to get the actual date if available
                total: total,
                products: orderProducts
            };
        });

        return orders;
        
    } catch (error) {
        console.error("Failed to fetch orders:", error);
        return [];
    }
};
export const fetchUserStores = async (username: string): Promise<RoleModel[]> => {
    const response = await fetch(`${server}/api/user/getUserRoles?username=${username}`,{
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    });
    const res= await response.json() ; 
    const stores= JSON.parse(res.dataJson) as RoleModel[];
    return stores;
};

export const fetchNotifications = async (): Promise<NotificationModel[]> => {
    const response = await fetch(
        `${server}/api/user/getNotifications?username=${localStorage.getItem("username")}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    if(data.error){
        return [];
    }
    // Assuming the API returns the data in dataJson
    const notifs = JSON.parse(data.dataJson) as NotificationModel[];
    return notifs
};

export const createNewStore = async (storeModel: CreateStoreModel,storeFounder :string=localStorage.getItem("username") as string) => {
    storeModel.founderUsername=storeFounder;
    const response = await axios.post(`${server}/api/stores/createStore`, storeModel,{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    return response.data;
};
export const addProductToCartMember = async (productId:number,storeId:number, amount:number) => {
    const response = await axios.patch(`${server}/api/user/addProductToCart?username=${localStorage.getItem("username")}`,{storeId: storeId, productId:productId,amount:amount},{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    return response.data;
}
export const addProductToCartGuest = async (productId:number,storeId:number, amount:number) => {
    const response = await axios.patch(`${server}/api/user/addProductToCart?guestId=${localStorage.getItem("guestId")}`,{storeId: storeId, productId:productId,amount:amount}
    );
    return response.data;
}

export const changeProductAmountInCart = async (productId:number,storeId:number, amount:number) => {
    const response = await axios.patch(`${server}/api/user/changeQuantityCart?username=${localStorage.getItem("username")}`,{storeId: storeId, productId:productId,amount:amount,owner:localStorage.getItem("username") as string},{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    return response.data;
}
export const changeProductAmountInCartGuest = async (productId:number,storeId:number, amount:number) => {
    const response = await axios.patch(`${server}/api/user/guest/changeQuantityCart?guestId=${localStorage.getItem("guestId")}`,{storeId: storeId, productId:productId,amount:amount}
    );
    return response.data;
}

export const removeProductFromCart = async (productId:number,storeId:number) => {
    const response = await axios.patch(`${server}/api/user/removeProductFromCart?username=${localStorage.getItem("username")}`,{storeId: storeId, productId:productId},{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` 
        }
    }
    );
    return response.data;
}
export const removeProductFromCartGuest = async (productId:number,storeId:number) => {
    const response = await axios.patch(`${server}/api/user/guest/removeProductFromCart?guestId=${localStorage.getItem("guestId")}`,{storeId: storeId, productId:productId}
    );
    return response.data;
}
export const checkCart= async (username:string) => {
    const response = await axios.post(`${server}/api/user/checkMemberCart?username=${localStorage.getItem("username")}`,{},{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` 
        }
    }
    );
    return response.data;
}
export const checkCartGuest= async (guestId:number) => {
    const response = await axios.post(`${server}/api/user/checkGuestCart?guestId=${guestId}`,{});
    return response.data;
}

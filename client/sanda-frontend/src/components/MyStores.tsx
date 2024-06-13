// components/Stores.tsx
import React, { useEffect, useState, useContext } from 'react';
import { useParams } from 'react-router-dom';
import { fetchUserStores, createNewStore } from '../API';
import RoleModel from '../models/RoleModel';
import '../styles/stores.css';
import { AppContext } from '../App';

const Stores = () => {
    const { username } = useParams<{ username: string }>();
    const [stores, setStores] = useState<RoleModel[]>([]);
    const [newStoreName, setNewStoreName] = useState('');
    const { isloggedin } = useContext(AppContext);

    useEffect(() => {
        if (username) {
            fetchUserStores(username).then(setStores);
        }
    }, [username]);

    const handleCreateStore = () => {
        if (username && newStoreName) {
            createNewStore(username, newStoreName).then(newStore => {
                setStores([...stores, newStore]);
                setNewStoreName('');
            });
        }
    };

    return (
        <div className="stores-container">
            <h2>User's Stores</h2>
            <button className="create-store-button" onClick={handleCreateStore}>Create New Store</button>
            <div className="stores-grid">
                {stores.map((store) => (
                    <div key={store.storeId} className="store-card">
                        <h3>{store.storeName}</h3>
                        <p>Role: {store.roleName}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Stores;

import React from "react";
import Cours from "../components/Cours";

const Evolution: React.FC = () => {
    const items = [
        { name: 'Article 1', amount: 100 },
        { name: 'Article 2', amount: 200 },
        { name: 'Article 3', amount: 300 },
    ];


    return (
        <div className="home">
            <h1>Liste des Articles</h1>
            {items.map((item, index) => (
                <Cours key={index} name={item.name} amount={item.amount} />
            ))}
        </div>
    );
};

export default Evolution;
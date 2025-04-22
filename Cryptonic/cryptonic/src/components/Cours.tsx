// ItemDisplay.tsx
import React from 'react';

interface CoursProps {
    name: string;
    amount: number;
}

const Cours: React.FC<CoursProps> = ({ name, amount }) => {
    return (
        <div className="item-display">
            <h2>{name}</h2>
            <p>Montant: {amount} €</p>
        </div>
    );
};

export default Cours;
import React, { useEffect, useState } from 'react';

const Test = () => {
    const [machines, setMachines] = useState([]);

    useEffect(() => {
        const fetchMachines = async () => {
            try {
                const response = await fetch('http://localhost:8080/machines');
                const data = await response.json();
                setMachines(data);
            } catch (error) {
                console.error('Error fetching machines:', error);
            }
        };

        fetchMachines();
    }, []);

    return (
        <div>
            <h1>Arduino Data</h1>
            <ul>
                {machines.map(machine => (
                    <li key={machine.id}>{machine.dosageCreatedAt}</li>
                ))}
            </ul>
        </div>
    );
};

export default Test;

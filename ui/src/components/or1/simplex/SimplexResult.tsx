import React from 'react';


export interface ResultOfSimplex {
    simplexTableaus: SimplexResultProps[];
    isUnlimited: boolean;
}

export interface SimplexResultProps {
    simplexTable: number[][];
    baseVariables: string[];
    nonBaseVariables: string[];
    rightSide: number[];
    goalCoefficients: number[];
    goalFunctionValue: number;
    notes: string;

}

const SimplexResult = ({simplexTableaus, isUnlimited}: ResultOfSimplex) => {
    simplexTableaus = roundSimplexTableaus(simplexTableaus);
    return (
        <div className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            <h2 className="text-lg font-bold mb-4">Simplex Algorithm Result</h2>
            <p className="text-gray-700">{isUnlimited ? 'Solution is unlimited' : 'Optimal solution found'}</p>

            {simplexTableaus.map((tableau, index) => (
                <div key={index} className="my-4">
                    <h3 className="text-lg font-bold mb-2">Tableau {index + 1}</h3>
                    <table className="table-auto">
                        <thead>
                        <tr>
                            <th className="px-4 py-2 border"></th>
                            {tableau.nonBaseVariables.map((variable, i) => (
                                <th key={i} className="px-4 py-2 border">{variable}</th>
                            ))}
                            <th className="px-4 py-2 border">Right Side</th>
                        </tr>
                        <tr>
                            <th className="px-4 py-2 border">Z</th>
                            {tableau.goalCoefficients.map((coefficient, i) => (
                                <td key={i} className="px-4 py-2 border">{coefficient}</td>
                            ))}
                            <td className="px-4 py-2 border">{tableau.goalFunctionValue}</td>
                        </tr>
                        </thead>
                        <tbody>
                        {tableau.simplexTable.map((row, i) => (
                            <tr key={i}>
                                <th className="px-4 py-2 border">{tableau.baseVariables[i]}</th>
                                {row.map((cell, j: number) => (
                                    <td key={j} className="px-4 py-2 border">{cell}</td>
                                ))}
                                <td className="px-4 py-2 border">{tableau.rightSide[i]}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    <p className="text-gray-700">Goal function value: {tableau.goalFunctionValue}</p>
                    {tableau.notes && <p className="text-gray-700">Notes: {tableau.notes}</p>}
                </div>
            ))}
        </div>
    );
};
const roundSimplexTableaus = (simplexTableaus: SimplexResultProps[], digits= 3) => {
    return simplexTableaus.map((simplexTableau) => {
        return {
            ...simplexTableau,
            simplexTable: simplexTableau.simplexTable.map((row) => {
                return row.map((value) => {
                    return roundValue(value, digits)
                })
            }),
            goalFunctionValue: Math.round(simplexTableau.goalFunctionValue * 100) / 100,
            goalCoefficients: simplexTableau.goalCoefficients.map((value) => {
                return roundValue(value, digits)
            }),
            rightSide: simplexTableau.rightSide.map((value) => {
                return roundValue(value, digits)
            })
        }
    })
}

export const roundValue = (value: number, digits: number) =>  {
    return Math.round(value * Math.pow(10, digits)) / Math.pow(10, digits)
}

export default SimplexResult;


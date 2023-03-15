import React from 'react';


interface ResultOfSimplex {
    simplexTableaus: SimplexResultProps[];
    isUnlimited: boolean;
}

interface SimplexResultProps {
    simplexTable: number[][];
    baseVariables: string[];
    nonBaseVariables: string[];
    rightSide: number[];
    goalCoefficients: number[];
    goalFunctionValue: number;
    notes: string;

}

const SimplexResult = ({ simplexTableaus, isUnlimited }: ResultOfSimplex) => {

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
                <th className="px-4 py-2"></th>
                {tableau.baseVariables.map((variable, i) => (
                  <th key={i} className="px-4 py-2">{variable}</th>
                ))}
                {tableau.nonBaseVariables.map((variable, i) => (
                  <th key={i} className="px-4 py-2">{variable}</th>
                ))}
                <th className="px-4 py-2">Right Side</th>
              </tr>
            </thead>
            <tbody>
              {tableau.simplexTable.map((row, i) => (
                <tr key={i}>
                  <th className="px-4 py-2">{i === 0 ? 'Z' : tableau.baseVariables[i - 1]}</th>
                  {row.map((cell: string | number | boolean | React.ReactFragment | React.ReactPortal | React.ReactElement<any, string | React.JSXElementConstructor<any>> | null | undefined, j: number) => (
                    <td key={j} className="border px-4 py-2">{cell}</td>
                  ))}
                  <td className="border px-4 py-2">{tableau.rightSide[i]}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <p className="text-gray-700 mt-4">Goal coefficients: {tableau.goalCoefficients.join(', ')}</p>
          <p className="text-gray-700">Goal function value: {tableau.goalFunctionValue}</p>
          <p className="text-gray-700">Notes: {tableau.notes}</p>
        </div>
      ))}
    </div>
  );
};

export default SimplexResult;
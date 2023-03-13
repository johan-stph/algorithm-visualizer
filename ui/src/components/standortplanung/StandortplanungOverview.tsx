import {Link} from "react-router-dom";

export default function StandortplanungOverview() {

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
            <h1 className="text-3xl font-bold mb-8">Standortplanung</h1>
            <div className="flex flex-col md:flex-row">
                <Link to="/sp/cost-efficient-allocation" className="bg-indigo-500 hover:bg-indigo-700 text-white font-bold py-2 px-4 rounded mb-4 md:mb-0 md:mr-4">
                    Diskrete Standortplanung
                </Link>
            </div>
        </div>
    );
}
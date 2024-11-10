import "./App.css";
import Form from "./Components/Form";
import Lists from "./Components/Lists";
import Navigation from "./customer/components/Navigation/Navigation";
import HomePage from "./customer/Pages/HomePage/HomePage";

function App() {
  return (
    <>
      <div>
        <Navigation />
        <HomePage />
      </div>
      <div>
        <h1 className="text-3xl font-bold underline">Hello world!</h1>
        <Form />
        <Lists />
      </div>
    </>
  );
}

export default App;
